package com.example.stockia.common
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stockia.ui.theme.AzulPrincipal
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
            fontFamily = Poppins,
            fontSize = 16.sp,
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



