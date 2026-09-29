package com.example.ui.components

import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.PlusJakartaSansFontFamily
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

/**
 * Logotipo oficial de "Mis Cuentas"
 * Muestra el ícono 3D de la billetera con tarjetas y detalles dorados.
 */
@Composable
fun MisCuentasIcon(
    size: Dp = 40.dp,
    backgroundColor: Color = Color(0xFF0F2D3A),
    iconColor: Color = Color.White,
    accentColor: Color = GoldPrimary,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = R.drawable.ic_app_wallet_icon),
        contentDescription = "Mis Cuentas",
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(size * 0.25f))
    )
}

/**
 * Logotipo horizontal completo: [ICON] Mis Cuentas / Tu dinero, en orden
 * Estilizado con "Mis" en blanco (#EDEDED) y "Cuentas" en dorado (#C79A3A).
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
            backgroundColor = Color(0xFF0F2D3A),
            iconColor = Color.White,
            accentColor = GoldPrimary
        )

        Spacer(modifier = Modifier.width(10.dp))

        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Mis ",
                    fontFamily = PlusJakartaSansFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = TextPrimary
                )
                Text(
                    text = "Cuentas",
                    fontFamily = PlusJakartaSansFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = GoldPrimary
                )
            }
            if (showTagline) {
                Text(
                    text = "Tu dinero, en orden",
                    fontFamily = PlusJakartaSansFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 11.sp,
                    color = TextSecondary,
                    letterSpacing = 0.2.sp
                )
            }
        }
    }
}
