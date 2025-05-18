package com.example.stockia.view.categories

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.stockia.common.CommonError
import com.example.stockia.common.CustomButtonBlue
import com.example.stockia.common.CustomTextField
import com.example.stockia.common.HeaderWithBackArrow
import com.example.stockia.routes.Routes
import com.example.stockia.ui.theme.AppTypography
import com.example.stockia.ui.theme.AzulPrincipal
import com.example.stockia.ui.theme.BlancoBase
import com.example.stockia.ui.theme.Subtitulos
import com.example.stockia.viewmodel.categories.EditCategoryViewModel

@Composable
fun EditCategoryView(
    navController: NavController,
    categoryId: Int,
    viewModel: EditCategoryViewModel = viewModel()
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(categoryId) {
        viewModel.loadCategory(categoryId)
    }

    LaunchedEffect(viewModel.resultMessage) {
        when (viewModel.resultMessage) {
            "success" -> {
                if (viewModel.wasEditing){
                    Toast.makeText(context, "Categoria modificada exitosamente", Toast.LENGTH_LONG).show()
                    viewModel.wasEditing = false
                }
                else {
                    Toast.makeText(context, "Categoria eliminada exitosamente", Toast.LENGTH_LONG).show()
                }
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
    ) {        Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HeaderWithBackArrow(
            text = "Editar categoría",
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
        viewModel.nameError?.let {
            CommonError(text = it)
        }
        Spacer(Modifier.height(32.dp))

        CustomButtonBlue(
            text = if (!viewModel.isLoading) "Guardar" else "Guardando",
            enabled = viewModel.isFormValid && !viewModel.isLoading ,
            onClick = { viewModel.onUpdateClick(categoryId) }
        )

        Spacer(Modifier.height(16.dp))

        TextButton(onClick = { viewModel.onDeleteClick(categoryId)  }) {
            Text("Eliminar categoría", color = AzulPrincipal, fontSize = 16.sp,)
        }


    }
    }
}
