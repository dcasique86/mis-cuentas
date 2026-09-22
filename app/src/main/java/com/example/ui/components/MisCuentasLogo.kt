package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.Coral
import com.example.ui.theme.DeepGreen
import com.example.ui.theme.PoppinsFontFamily
import com.example.ui.theme.SoftGreen
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

/**
 * Logotipo oficial de "Mis Cuentas"
 * Simboliza dinero, orden, movimiento y control:
 * Cartera / tarjeta estilizada con un pliegue suave y el punto coral icónico de energía/moneda.
 */
@Composable
fun MisCuentasIcon(
    size: Dp = 40.dp,
    backgroundColor: Color = DeepGreen,
    iconColor: Color = Color.White,
    accentColor: Color = Coral,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(size * 0.28f))
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size * 0.65f)) {
            val w = this.size.width
            val h = this.size.height

            // 1. Tilted top flap of the wallet/envelope
            val flapPath = Path().apply {
                moveTo(w * 0.15f, h * 0.40f)
                lineTo(w * 0.55f, h * 0.08f)
                // soft curve to right
                quadraticTo(w * 0.72f, h * 0.06f, w * 0.75f, h * 0.22f)
                lineTo(w * 0.35f, h * 0.48f)
                close()
            }
            drawPath(
                path = flapPath,
                color = iconColor.copy(alpha = 0.85f)
            )

            // 2. Main wallet / card body (rounded curved shape)
            val bodyPath = Path().apply {
                addRoundRect(
                    RoundRect(
                        left = w * 0.05f,
                        top = h * 0.28f,
                        right = w * 0.95f,
                        bottom = h * 0.92f,
                        topLeftCornerRadius = CornerRadius(w * 0.22f, h * 0.22f),
                        topRightCornerRadius = CornerRadius(w * 0.25f, h * 0.25f),
                        bottomRightCornerRadius = CornerRadius(w * 0.22f, h * 0.22f),
                        bottomLeftCornerRadius = CornerRadius(w * 0.22f, h * 0.22f)
                    )
                )
            }
            drawPath(
                path = bodyPath,
                color = iconColor
            )

            // 3. Iconic coral coin / accent dot on the right side
            drawCircle(
                color = accentColor,
                radius = w * 0.15f,
                center = Offset(w * 0.70f, h * 0.58f)
            )
        }
    }
}

/**
 * Logotipo horizontal completo: [ICON] Mis Cuentas / Tu dinero, en orden
 */
@Composable
fun MisCuentasHeaderLogo(
    modifier: Modifier = Modifier,
    iconSize: Dp = 38.dp,
    showTagline: Boolean = true
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MisCuentasIcon(
            size = iconSize,
            backgroundColor = DeepGreen,
            iconColor = Color.White,
            accentColor = Coral
        )

        Spacer(modifier = Modifier.width(10.dp))

        Column {
            Text(
                text = "Mis Cuentas",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 19.sp,
                color = TextPrimary,
                lineHeight = 22.sp
            )
            if (showTagline) {
                Text(
                    text = "Tu dinero, en orden",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 11.sp,
                    color = TextSecondary,
                    letterSpacing = 0.4.sp,
                    lineHeight = 14.sp
                )
            }
        }
    }
}
