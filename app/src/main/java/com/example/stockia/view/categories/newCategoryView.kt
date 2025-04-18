package com.example.stockia.view.categories


import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.stockia.R
import com.example.stockia.common.CategoryTextButton
import com.example.stockia.common.CustomButtonBlue
import com.example.stockia.common.CustomTextField
import com.example.stockia.common.HeaderWithBackArrow
import com.example.stockia.routes.Routes
import com.example.stockia.routes.Routes.newCategoryView
import com.example.stockia.ui.theme.AppTypography
import com.example.stockia.ui.theme.BlancoBase
import com.example.stockia.ui.theme.StockIATheme
import com.example.stockia.ui.theme.Subtitulos
import com.example.stockia.viewmodel.categories.NewCategoryViewModel


@Composable
fun newCategoryView(
    navController: NavController,
    viewModel: NewCategoryViewModel = viewModel()
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    // Escuchar mensajes de resultado para toast y navegación
    LaunchedEffect(viewModel.resultMessage) {
        when (viewModel.resultMessage) {
            "success" -> {
                Toast.makeText(context, "Categoria agregada exitosamente", Toast.LENGTH_LONG).show()
                viewModel.clearResultMessage()
                navController.popBackStack()
            }

            null -> Unit

            else -> {
                Toast.makeText(context, viewModel.resultMessage, Toast.LENGTH_LONG).show()
                viewModel.clearResultMessage()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .background(BlancoBase)
            .padding(24.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            HeaderWithBackArrow(
                text = "Nueva categoría",
                onClick = { navController.popBackStack() }
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Nombre",
                style = AppTypography.bodyLarge,
                color = Subtitulos,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(8.dp))

            CustomTextField(
                value = viewModel.name,
                onValueChange = viewModel::onNameChange,
                label = "Nombre de la categoría",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(32.dp))

            CustomButtonBlue(
                text = if (!viewModel.isLoading) "Crear categoría" else "Creando",
                enabled = viewModel.isFormValid && !viewModel.isLoading,
                onClick = { viewModel.onCreateClick() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun newCategoryViewPreview() {
    StockIATheme {
        newCategoryView(navController = rememberNavController())
    }
}