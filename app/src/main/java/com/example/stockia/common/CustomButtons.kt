package com.example.stockia.common
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stockia.R
import com.example.stockia.ui.theme.AppTypography
import com.example.stockia.ui.theme.AzulPrincipal
import com.example.stockia.ui.theme.BlancoBase
import com.example.stockia.ui.theme.Poppins
import com.example.stockia.ui.theme.StockIATheme


@Composable
fun CustomButtonBlue(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (enabled) AzulPrincipal else Color.Gray
        )
    ) {
        Text(
            text = text,
            style = AppTypography.bodyLarge,
            color = Color.White


        )
    }
}

@Preview
@Composable
fun CustomButtonBluePreview() {
    StockIATheme {
        CustomButtonBlue(
            text = "Iniciar sesión",
            enabled = true,
            onClick = { }
        )
    }
}

@Composable
fun IconTextButton(
    text: String,
    @DrawableRes iconRes: Int,
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,  // Altura de la sombra
                shape = RoundedCornerShape(14.dp),
                clip = false,  // Permite que la sombra se vea fuera del límite del componente
                spotColor = Color.Gray.copy(alpha = 0.5f)  // Color de la sombra
            )
            .height(90.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = BlancoBase,
        ),
        contentPadding = PaddingValues(vertical = 12.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = text,
                tint = AzulPrincipal,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = text,
                fontSize = 16.sp,
                style = AppTypography.titleSmall,
                color = Color.Black

            )
        }
    }
}

@Preview(showBackground = false)
@Composable
fun IconTextButtonPreview() {
    StockIATheme {
        IconTextButton(
            text = "Categorías",
            iconRes = R.drawable.categorias,
            onClick = { /* acción */ }
        )
    }
}

@Composable
fun CategoryTextButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,  // Altura de la sombra
                shape = RoundedCornerShape(14.dp),
                clip = false,  // Permite que la sombra se vea fuera del límite del componente
                spotColor = Color.Gray.copy(alpha = 0.5f)  // Color de la sombra
            )
            .heightIn(min = 60.dp),  // Altura mínima pero puede expandirse
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor =Color.White,
        ),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)  // Padding consistente
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = text,
                style = AppTypography.bodyLarge,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp),  // Espacio fijo entre texto e icono
                maxLines = 3,  // Permite 2 líneas máximo
                overflow = TextOverflow.Ellipsis  // Puntos suspensivos si no cabe
            )

            Image(
                painter = painterResource(id = R.drawable.lapiz),
                contentDescription = "Add icon",
                modifier = Modifier
                    .size(24.dp)  // Tamaño fijo
                    .clickable(onClick = onClick),
                alignment = Alignment.CenterEnd  // Alineación consistente
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 300)
@Composable
fun CategoryTextButtonPreview() {
    StockIATheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            CategoryTextButton(
                text = "Categorías",
                onClick = {}
            )

            CategoryTextButton(
                text = "Categorías extremadamente largas que necesitan mucho espacio",
                onClick = {}
            )
        }
    }
}




