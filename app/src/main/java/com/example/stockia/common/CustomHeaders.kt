package com.example.stockia.common


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import HomeViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.stockia.R
import com.example.stockia.ui.theme.BlancoBase
import com.example.stockia.ui.theme.StockIATheme


@Composable
fun HeaderWithBackArrow(
    modifier: Modifier = Modifier,
    text: String,
    onClick: (() -> Unit)? = null // ahora es opcional
) {
    val navController = rememberNavController()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .systemBarsPadding()
            .padding(8.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.back_arrow),
            contentDescription = "Back arrow",
            modifier = Modifier
                .size(30.dp)
                .clickable {
                    onClick?.invoke() ?: navController.popBackStack()
                }
        )

        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview
@Composable
fun HeaderWithBackArrowPreview(){
    StockIATheme {
        HeaderWithBackArrow(text = "Crear cuenta")
    }
}

@Composable
fun HeaderWithHamburger(
    modifier: Modifier = Modifier,
    text: String,
    onClick: (() -> Unit)? = null // ahora es opcional
) {
    val navController = rememberNavController()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .systemBarsPadding()
            .padding(8.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.hamburger),
            contentDescription = "Back arrow",
            modifier = Modifier
                .size(30.dp)
                .clickable {
                    onClick?.invoke() ?: navController.popBackStack()
                }
        )

        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview
@Composable
fun HeaderWithHamburgerPreview(){
    StockIATheme {
        HeaderWithHamburger(text = "Inventario")
    }
}

@Composable
fun SideMenu(
    modifier: Modifier = Modifier,
    currentRoute: String,
    nameUser: String = "",
    onItemClick: (route: String) -> Unit,
    onLogoutClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(text = nameUser, style = MaterialTheme.typography.titleSmall)
        }

        Spacer(Modifier.height(10.dp))
        Divider()

        MenuSection(
            title        = "Inventario",
            icon         = Icons.Default.Star,
            expanded     = currentRoute in listOf("inventario","productos","categorias","stock"),
            onHeaderClick= { onItemClick("inventario") }
        ) {
            MenuItem("Productos", icon = Icons.Default.Star, selected = currentRoute == "productos") {
                onItemClick("productos")
            }
            MenuItem("Categorías", icon = Icons.Default.Star, selected = currentRoute == "categorias") {
                onItemClick("categorias")
            }
            MenuItem("Stock", icon = Icons.Default.Star, selected = currentRoute == "stock") {
                onItemClick("stock")
            }
        }

        MenuSection(
            title        = "Ventas",
            icon         = Icons.Default.Star,
            expanded     = currentRoute in listOf("ventas","ordenes_de_venta","devoluciones_de_venta"),
            onHeaderClick= { onItemClick("ventas") }
        ) {
            MenuItem("Órdenes de venta", icon = Icons.Default.Star, selected = currentRoute == "ordenes_de_venta") {
                onItemClick("ordenes_de_venta")
            }
            MenuItem("Devoluciones",    icon = Icons.Default.Star, selected = currentRoute == "devoluciones_de_venta") {
                onItemClick("devoluciones_de_venta")
            }
        }

        MenuSection(
            title        = "Pedidos",
            icon         = Icons.Default.Star,
            expanded     = currentRoute in listOf("pedidos","ordenes_de_compra","devoluciones_de_compra"),
            onHeaderClick= { onItemClick("pedidos") }
        ) {
            MenuItem("Órdenes de compra",  icon = Icons.Default.Star, selected = currentRoute == "ordenes_de_compra") {
                onItemClick("ordenes_de_compra")
            }
            MenuItem("Devoluciones compra", icon = Icons.Default.Star, selected = currentRoute == "devoluciones_de_compra") {
                onItemClick("devoluciones_de_compra")
            }
        }

        Spacer(Modifier.height(8.dp))

        MenuHeaderItem(
            title    = "Clientes",
            icon     = Icons.Default.Star,
            selected = currentRoute == "clientes"
        ) {
            onItemClick("clientes")
        }
        MenuHeaderItem(
            title    = "Proveedores",
            icon     = Icons.Default.Star,
            selected = currentRoute == "proveedores"
        ) {
            onItemClick("proveedores")
        }
        MenuHeaderItem(
            title    = "Estadísticas",
            icon     = Icons.Default.Star,
            selected = currentRoute == "estadisticas"
        ) {
            onItemClick("estadisticas")
        }


        Spacer(Modifier.weight(1f))
        CustomButtonBlue(
            text    = "Cerrar sesión",
            onClick = onLogoutClick,
            enabled = true
        )
    }
}

@Composable
fun MenuHeaderItem(
    title: String,
    icon: ImageVector,
    selected: Boolean = false,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (selected)
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                else Color.Transparent
            )
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = if (selected)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = if (selected)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun MenuSection(
    title: String,
    icon: ImageVector,
    expanded: Boolean,
    onHeaderClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit = {}
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onHeaderClick)
                .padding(vertical = 12.dp)
        ) {
            Icon(icon, contentDescription = null, Modifier.size(20.dp))
            Spacer(Modifier.width(12.dp))
            Text(title, style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.weight(1f))
            Icon(
                imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = null
            )
        }
        if (expanded) {
            Column(Modifier.padding(start = 24.dp)) {
                content()
            }
        }
    }
}

@Composable
fun MenuItem(
    title: String,
    icon: ImageVector,
    selected: Boolean = false,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                else Color.Transparent
            )
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            Modifier.size(18.dp),
            tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}


@Preview(
    showBackground = true,
    widthDp = 280,
    heightDp = 640)
@Composable
fun SideMenuInteractivePreview() {
    var currentRoute by remember { mutableStateOf("") }

    StockIATheme {
        SideMenu(
            currentRoute   = currentRoute,
            nameUser       = "Natalia",
            onItemClick    = { route ->
                // toggle expand/collapse
                currentRoute = if (currentRoute == route) "" else route
            },
            onLogoutClick  = { /* nada */ }
        )
    }
}
