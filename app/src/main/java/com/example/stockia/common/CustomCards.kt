package com.example.stockia.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import com.example.stockia.ui.theme.AppTypography
import com.example.stockia.ui.theme.AzulPrincipal

@Composable
fun ProductCardSelectable(
    productName: String,
    categoryName: String,
    price: String,
    quantityAvailable: String,
    imageUrl: String?,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) AzulPrincipal else Color.LightGray

    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        border = BorderStroke(1.dp, borderColor),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = imageUrl?.let { "http://173.212.224.226:3000$it" },
                contentDescription = "Imagen producto",
                placeholder = rememberVectorPainter(Icons.Default.Photo),
                error = rememberVectorPainter(Icons.Default.Photo),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray.copy(alpha = 0.2f))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = productName,
                    style = AppTypography.bodyLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Categoría: $categoryName", style = AppTypography.bodySmall)
                Text(text = "Precio: $$price", style = AppTypography.bodySmall)
                Text(text = "Disponibles: $quantityAvailable", style = AppTypography.bodySmall)
            }
        }
    }
}

@Preview
@Composable
fun ProductCardSelectablePreview() {
    ProductCardSelectable(
        productName = "Producto X",
        categoryName = "Categoría Y",
        price = "10.99",
        quantityAvailable = "15",
        imageUrl = null,
        isSelected = false,
        onClick = {}
    )
}





