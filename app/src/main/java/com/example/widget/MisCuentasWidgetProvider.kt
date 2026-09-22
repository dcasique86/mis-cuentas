package com.example.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.MainActivity
import com.example.R
import com.example.data.AppDatabase
import com.example.data.entity.TransactionEntity
import com.example.ui.components.Formatters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object WidgetHelper {
    const val ACTION_REFRESH_ALL_WIDGETS = "com.example.widget.ACTION_REFRESH_ALL_WIDGETS"

    fun updateAllWidgets(context: Context) {
        val appWidgetManager = AppWidgetManager.getInstance(context)

        // Compact
        val compactComponent = ComponentName(context, MisCuentasCompactWidgetProvider::class.java)
        val compactIds = appWidgetManager.getAppWidgetIds(compactComponent)
        for (id in compactIds) {
            updateCompactWidget(context, appWidgetManager, id)
        }

        // Standard
        val standardComponent = ComponentName(context, MisCuentasStandardWidgetProvider::class.java)
        val standardIds = appWidgetManager.getAppWidgetIds(standardComponent)
        for (id in standardIds) {
            updateStandardWidget(context, appWidgetManager, id)
        }

        // Extended
        val extendedComponent = ComponentName(context, MisCuentasExtendedWidgetProvider::class.java)
        val extendedIds = appWidgetManager.getAppWidgetIds(extendedComponent)
        for (id in extendedIds) {
            updateExtendedWidget(context, appWidgetManager, id)
        }
    }

    private fun getRefreshPendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, MisCuentasStandardWidgetProvider::class.java).apply {
            action = ACTION_REFRESH_ALL_WIDGETS
        }
        return PendingIntent.getBroadcast(
            context,
            99,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun getOpenAppPendingIntent(context: Context, quickAction: String? = null, requestCode: Int = 0): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            if (quickAction != null) {
                putExtra(MainActivity.EXTRA_QUICK_ACTION, quickAction)
            }
        }
        return PendingIntent.getActivity(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private suspend fun calculateFinancialData(context: Context): WidgetFinancialData {
        val db = AppDatabase.getDatabase(context)
        val transactions: List<TransactionEntity> = db.transactionDao().getAllTransactions().first()

        var totalBalance = 0.0
        val now = Calendar.getInstance()
        val cal = Calendar.getInstance()
        var todayIncome = 0.0
        var todayExpense = 0.0

        for (tx in transactions) {
            if (tx.isIncome) {
                totalBalance += tx.amount
            } else {
                totalBalance -= tx.amount
            }

            cal.timeInMillis = tx.timestamp
            val isToday = cal.get(Calendar.YEAR) == now.get(Calendar.YEAR) &&
                    cal.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR)
            if (isToday) {
                if (tx.isIncome) {
                    todayIncome += tx.amount
                } else {
                    todayExpense += tx.amount
                }
            }
        }

        val todayDiff = todayIncome - todayExpense
        val todayDiffStr = when {
            todayDiff > 0 -> "↑ + ${Formatters.formatMoney(todayDiff)} hoy"
            todayDiff < 0 -> "↓ - ${Formatters.formatMoney(-todayDiff)} hoy"
            else -> "— $0 hoy"
        }

        val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
        val timeFormatted = "Actualizado ${timeFormat.format(now.time)}"

        return WidgetFinancialData(
            totalBalance = totalBalance,
            todayIncome = todayIncome,
            todayExpense = todayExpense,
            todayDiff = todayDiff,
            todayDiffText = todayDiffStr,
            updatedTime = timeFormatted
        )
    }

    fun updateCompactWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val data = calculateFinancialData(context)
                val views = RemoteViews(context.packageName, R.layout.widget_mis_cuentas_compact)

                views.setTextViewText(R.id.widget_compact_balance, Formatters.formatMoney(data.totalBalance))
                views.setTextViewText(R.id.widget_compact_today_diff, data.todayDiffText)

                // Color of diff text
                val diffColor = when {
                    data.todayDiff > 0 -> 0xFF1E7E45.toInt()
                    data.todayDiff < 0 -> 0xFFD93025.toInt()
                    else -> 0xFF7A8B82.toInt()
                }
                views.setTextColor(R.id.widget_compact_today_diff, diffColor)

                // Click handlers
                views.setOnClickPendingIntent(R.id.widget_compact_container, getOpenAppPendingIntent(context, null, 10))
                views.setOnClickPendingIntent(R.id.widget_compact_btn_refresh, getRefreshPendingIntent(context))

                appWidgetManager.updateAppWidget(appWidgetId, views)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateStandardWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val data = calculateFinancialData(context)
                val views = RemoteViews(context.packageName, R.layout.widget_mis_cuentas_standard)

                views.setTextViewText(R.id.widget_standard_balance, Formatters.formatMoney(data.totalBalance))
                views.setTextViewText(R.id.widget_standard_today_diff, data.todayDiffText)
                views.setTextViewText(R.id.widget_standard_income, Formatters.formatMoney(data.todayIncome))
                views.setTextViewText(R.id.widget_standard_expense, Formatters.formatMoney(data.todayExpense))

                val diffColor = when {
                    data.todayDiff > 0 -> 0xFF1E7E45.toInt()
                    data.todayDiff < 0 -> 0xFFD93025.toInt()
                    else -> 0xFF7A8B82.toInt()
                }
                views.setTextColor(R.id.widget_standard_today_diff, diffColor)

                // Click handlers
                views.setOnClickPendingIntent(R.id.widget_standard_container, getOpenAppPendingIntent(context, null, 20))
                views.setOnClickPendingIntent(R.id.widget_standard_btn_refresh, getRefreshPendingIntent(context))
                views.setOnClickPendingIntent(
                    R.id.widget_standard_btn_income,
                    getOpenAppPendingIntent(context, MainActivity.ACTION_ADD_INCOME, 21)
                )
                views.setOnClickPendingIntent(
                    R.id.widget_standard_btn_expense,
                    getOpenAppPendingIntent(context, MainActivity.ACTION_ADD_EXPENSE, 22)
                )

                appWidgetManager.updateAppWidget(appWidgetId, views)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateExtendedWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val data = calculateFinancialData(context)
                val views = RemoteViews(context.packageName, R.layout.widget_mis_cuentas_extended)

                views.setTextViewText(R.id.widget_extended_balance, Formatters.formatMoney(data.totalBalance))
                views.setTextViewText(R.id.widget_extended_today_diff, data.todayDiffText)
                views.setTextViewText(R.id.widget_extended_income, Formatters.formatMoney(data.todayIncome))
                views.setTextViewText(R.id.widget_extended_expense, Formatters.formatMoney(data.todayExpense))

                val balanceFormatted = when {
                    data.todayDiff > 0 -> "+ ${Formatters.formatMoney(data.todayDiff)}"
                    data.todayDiff < 0 -> "- ${Formatters.formatMoney(-data.todayDiff)}"
                    else -> "$ 0"
                }
                views.setTextViewText(R.id.widget_extended_net_balance, balanceFormatted)
                views.setTextViewText(R.id.widget_extended_updated_time, data.updatedTime)

                // Click handlers
                views.setOnClickPendingIntent(R.id.widget_extended_container, getOpenAppPendingIntent(context, null, 30))
                views.setOnClickPendingIntent(R.id.widget_extended_btn_refresh, getRefreshPendingIntent(context))
                views.setOnClickPendingIntent(
                    R.id.widget_extended_btn_income,
                    getOpenAppPendingIntent(context, MainActivity.ACTION_ADD_INCOME, 31)
                )
                views.setOnClickPendingIntent(
                    R.id.widget_extended_btn_expense,
                    getOpenAppPendingIntent(context, MainActivity.ACTION_ADD_EXPENSE, 32)
                )
                views.setOnClickPendingIntent(
                    R.id.widget_extended_btn_transfer,
                    getOpenAppPendingIntent(context, MainActivity.ACTION_ADD_TRANSFER, 33)
                )
                views.setOnClickPendingIntent(
                    R.id.widget_extended_btn_more,
                    getOpenAppPendingIntent(context, MainActivity.ACTION_NAVIGATE_MAS, 34)
                )

                appWidgetManager.updateAppWidget(appWidgetId, views)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

data class WidgetFinancialData(
    val totalBalance: Double,
    val todayIncome: Double,
    val todayExpense: Double,
    val todayDiff: Double,
    val todayDiffText: String,
    val updatedTime: String
)

/**
 * 1. Compact Widget (2x2)
 */
class MisCuentasCompactWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        for (id in appWidgetIds) {
            WidgetHelper.updateCompactWidget(context, appWidgetManager, id)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == AppWidgetManager.ACTION_APPWIDGET_UPDATE ||
            intent.action == WidgetHelper.ACTION_REFRESH_ALL_WIDGETS) {
            WidgetHelper.updateAllWidgets(context)
        }
    }
}

/**
 * 2. Standard Widget (4x2)
 */
open class MisCuentasStandardWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        for (id in appWidgetIds) {
            WidgetHelper.updateStandardWidget(context, appWidgetManager, id)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == AppWidgetManager.ACTION_APPWIDGET_UPDATE ||
            intent.action == WidgetHelper.ACTION_REFRESH_ALL_WIDGETS) {
            WidgetHelper.updateAllWidgets(context)
        }
    }
}

/**
 * Compatibility alias for MisCuentasWidgetProvider
 */
class MisCuentasWidgetProvider : MisCuentasStandardWidgetProvider()

/**
 * 3. Extended Widget (4x3 / 4x4)
 */
class MisCuentasExtendedWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        for (id in appWidgetIds) {
            WidgetHelper.updateExtendedWidget(context, appWidgetManager, id)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == AppWidgetManager.ACTION_APPWIDGET_UPDATE ||
            intent.action == WidgetHelper.ACTION_REFRESH_ALL_WIDGETS) {
            WidgetHelper.updateAllWidgets(context)
        }
    }
}
