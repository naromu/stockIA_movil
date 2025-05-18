package com.example.stockia.view.products

import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.stockia.R
import com.example.stockia.common.CommonError
import com.example.stockia.common.CustomTextField
import com.example.stockia.common.DropdownField
import com.example.stockia.common.HeaderWithBackArrow
import com.example.stockia.common.CustomButtonBlue
import com.example.stockia.common.CustomNumericTextField
import com.example.stockia.scanner.PortraitCaptureActivity
import com.example.stockia.ui.theme.AppTypography
import com.example.stockia.ui.theme.AzulPrincipal
import com.example.stockia.ui.theme.BlancoBase
import com.example.stockia.ui.theme.StockIATheme
import com.example.stockia.ui.theme.Subtitulos
import com.example.stockia.viewmodel.products.EditProductViewModel
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import com.journeyapps.barcodescanner.ScanOptions.ALL_CODE_TYPES
import java.io.ByteArrayOutputStream

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductView(
    navController: NavController,
    productId: Int,
    viewModel: EditProductViewModel = viewModel()
) {
    val context = LocalContext.current

    LaunchedEffect(productId) { viewModel.loadAll(productId) }

    val barcodeLauncher = rememberLauncherForActivityResult(ScanContract()) { res: ScanIntentResult ->
        res.contents?.let(viewModel::onBarCodeChange)
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        bitmap?.let { viewModel.onImageSelected(it) }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch()
        } else {
            Toast.makeText(context, "Se necesita permiso de cámara", Toast.LENGTH_LONG).show()
        }
    }


    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val bmp = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
            viewModel.onImageSelected(bmp)
        }
    }

    LaunchedEffect(viewModel.resultMessage) {
        viewModel.resultMessage?.let { msg ->
            if (msg == "success") {
                Toast.makeText(context, "Producto actualizado", Toast.LENGTH_LONG).show()
                navController.popBackStack()
            } else {
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                viewModel.clearResult()
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
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            HeaderWithBackArrow(
                text = "Editar producto",
                onClick = { navController.popBackStack() }
            )

            Spacer(Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .size(128.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFE0E0E0)),
                contentAlignment = Alignment.Center
            ) {
                when {
                    viewModel.imageBitmap != null -> {
                        Image(
                            bitmap = viewModel.imageBitmap!!.asImageBitmap(),
                            contentDescription = "Foto del producto",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        IconButton(
                            onClick = { viewModel.onClearImage() },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .background(Color(0x88000000), shape = CircleShape)
                                .size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Eliminar foto",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    viewModel.origImageUrl != null && !viewModel.imageChanged -> {
                        AsyncImage(
                            model = "http://173.212.224.226:3000${viewModel.origImageUrl}",
                            contentDescription = "Foto original del producto",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            placeholder = rememberVectorPainter(Icons.Default.Photo),
                            error = rememberVectorPainter(Icons.Default.Photo)
                        )
                    }

                    else -> {
                        Icon(
                            imageVector = Icons.Default.AddAPhoto,
                            contentDescription = "Seleccionar foto",
                            tint = Color.DarkGray,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
            }


            Spacer(Modifier.height(8.dp))

            Row( horizontalArrangement = Arrangement.spacedBy(16.dp) ) {
                IconButton(onClick = {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraLauncher.launch()
                    } else {
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                }) {
                    Icon(imageVector = Icons.Default.CameraAlt, contentDescription = "Abrir cámara")
                }

                IconButton(onClick = { galleryLauncher.launch("image/*") }) {
                    Icon(Icons.Default.PhotoLibrary, contentDescription = "Galería")
                }
            }
            viewModel.imageError?.let { CommonError(it) }

            Spacer(Modifier.height(24.dp))
            Text(
                text = "Nombre *",
                style = AppTypography.bodyLarge,
                color = Subtitulos,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(8.dp))

            CustomTextField(
                value = viewModel.name,
                onValueChange = viewModel::onNameChange,
                label = "Nombre del producto",
                modifier = Modifier.fillMaxWidth()
            )

            viewModel.nameError?.let {
                CommonError(text = it)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Descripción",
                style = AppTypography.bodyLarge,
                color = Subtitulos,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(8.dp))

            CustomTextField(
                value = viewModel.description,
                onValueChange = viewModel::onDescriptionChange,
                label = "Descripcion del producto",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Procentaje de ganancia: ${viewModel.getFormattedProfitPercentage()}",
                style = AppTypography.bodyLarge,
                color = Subtitulos,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(    modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column( modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Costo unitario *",
                        style = AppTypography.bodyLarge,
                        color = Subtitulos,
                        textAlign = TextAlign.Start
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    CustomNumericTextField(
                        value = viewModel.unitCost,
                        onValueChange = viewModel::onUnitCostChange,
                        label = "Costo unitario del producto",
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(        modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Precio unitario *",
                        style = AppTypography.bodyLarge,
                        color = Subtitulos,
                        textAlign = TextAlign.Start
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    CustomNumericTextField(
                        value = viewModel.unitPrice,
                        onValueChange = viewModel::onUnitPriceChange,
                        label = "Precio unitario del producto",
                    )
                }

            }

            viewModel.priceError?.let {
                CommonError(text = it)
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Cantidad",
                style = AppTypography.bodyLarge,
                color = Subtitulos,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(8.dp))

            CustomNumericTextField(
                value = viewModel.quantity,
                onValueChange = viewModel::onQuantityChange,
                label = "Cantidad del producto",
                modifier = Modifier.fillMaxWidth(),
                isInteger = true
            )

            Spacer(modifier = Modifier.height(16.dp))
            Row(    modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(        modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Unidad medida *",
                        style = AppTypography.bodyLarge,
                        color = Subtitulos,
                        textAlign = TextAlign.Start
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    DropdownField(
                        label="Unidad medida *",
                        options = viewModel.units.map{ it.id to "${it.name} (${it.symbol})" },
                        selected = viewModel.selectedUnitId,
                        onSelect = viewModel::onUnitSelected
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column( modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Categoria *",
                        style = AppTypography.bodyLarge,
                        color = Subtitulos,
                        textAlign = TextAlign.Start
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    DropdownField(
                        label="Categoría *",
                        options = viewModel.categories.map{ it.id to it.name },
                        selected = viewModel.selectedCategoryId,
                        onSelect = viewModel::onCategorySelected
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Código de barras",
                        style = AppTypography.bodyLarge,
                        color = Subtitulos,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CustomTextField(
                            value = viewModel.barCode,
                            onValueChange = viewModel::onBarCodeChange,
                            label = "Código de barras",
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Image(
                            painter = painterResource(R.drawable.barcode),
                            contentDescription = "Nuevo",
                            modifier = Modifier
                                .size(40.dp)
                                .clickable {
                                    val options = ScanOptions().apply {
                                        setDesiredBarcodeFormats(ALL_CODE_TYPES)
                                        setPrompt("Apunta al código de barras")
                                        setCaptureActivity(PortraitCaptureActivity::class.java)
                                    }
                                    barcodeLauncher.launch(options)
                                }
                        )
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            CustomButtonBlue(
                text = if (!viewModel.isLoading) "Actualizar" else "Actualizando...",
                enabled = viewModel.canUpdate && !viewModel.isLoading,
                onClick = { viewModel.onUpdateClick() }
            )

            Spacer(Modifier.height(16.dp))

            TextButton(onClick = {
                viewModel.onDeleteClick { navController.popBackStack() }
            }) {
                Text("Eliminar producto", color = AzulPrincipal, fontSize = 16.sp,)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditProductViewPreview() {
    StockIATheme {
   EditProductView(navController = rememberNavController(), productId = 3)    }
}