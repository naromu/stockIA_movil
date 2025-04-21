package com.example.stockia.viewmodel.products

import UnitOfMeasure
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockia.model.Category

import com.example.stockia.model.*
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.SocketTimeoutException

class EditProductViewModel : ViewModel() {
    companion object {
        private const val TAG = "EditProductVM"
    }

    private var productId: Int = -1

    private var orig: Product? = null

    var name by mutableStateOf("")

    var description by mutableStateOf("")

    var unitPrice by mutableStateOf("")

    var unitCost by mutableStateOf("")

    var quantity by mutableStateOf("")

    var barCode by mutableStateOf("")

    var imageBitmap by mutableStateOf<Bitmap?>(null)

    var origImageUrl: String? = null

    var profitPercentage by mutableStateOf<Double?>(null)
        private set

    var categories by mutableStateOf<List<Category>>(emptyList())

    var units by mutableStateOf<List<UnitOfMeasure>>(emptyList())

    var selectedCategoryId by mutableStateOf<Int?>(null)

    var selectedUnitId by mutableStateOf<Int?>(null)

    var nameError by mutableStateOf<String?>(null)

    var priceError by mutableStateOf<String?>(null)

    var costError by mutableStateOf<String?>(null)

    var categoryError by mutableStateOf<String?>(null)

    var unitError by mutableStateOf<String?>(null)

    var imageError by mutableStateOf<String?>(null)

    var imageChanged = false

    var isLoading by mutableStateOf(false)

    var resultMessage by mutableStateOf<String?>(null)

    fun onNameChange(v: String)      {
        name = v
        nameError = null
    }

    fun onDescriptionChange(v: String){
        description = v
    }

    fun loadAll(id: Int) {
        productId = id
        loadCategories()
        loadMeasurements()
        loadProduct(id)
        Log.d(TAG, "loadAll($id)")

    }

    private fun loadProduct(id: Int) = viewModelScope.launch {
        isLoading = true
        runCatching { RetrofitClient.api.getProduct(id) }
            .onSuccess { resp ->
                if (resp.isSuccessful && resp.body()?.status == "success") {
                    val p = resp.body()!!.data
                    orig = p
                    // poblar estado
                    name = p.name
                    description = p.description.orEmpty()
                    unitPrice = p.unitPrice.toString()
                    unitCost = p.unitCost.toString()
                    quantity = p.quantity
                        .toDoubleOrNull()
                        ?.toInt()
                        ?.toString()
                        ?: "0"
                    barCode = p.barcode.orEmpty()
                    selectedCategoryId = p.categoryId
                    selectedUnitId = p.unitOfMeasureId
                    origImageUrl = p.imageUrl
                    imageChanged = false

                    val cost = unitCost.toDoubleOrNull() ?: 0.0
                    val price = unitPrice.toDoubleOrNull() ?: 0.0
                    profitPercentage = if (cost > 0) ((price - cost) / cost) * 100 else 0.0

                    Log.d(TAG, "Loaded product: origImageUrl=$origImageUrl, imageChanged=$imageChanged")

                } else {
                    resultMessage = resp.body()?.message ?: "Error al cargar producto"
                    Log.d(TAG, "Load product failed: ${resp.code()} / ${resp.errorBody()?.string()}")

                }
            }
            .onFailure { resultMessage = it.localizedMessage }

        isLoading = false
    }

    private fun loadCategories() = viewModelScope.launch {
        runCatching { RetrofitClient.api.getCategories() }
            .onSuccess { resp ->
                if (resp.isSuccessful && resp.body()?.status == "success") {
                    categories = resp.body()!!.data
                }
            }
    }

    private fun loadMeasurements() = viewModelScope.launch {
        runCatching { RetrofitClient.api.getMeasurements() }
            .onSuccess { resp ->
                if (resp.isSuccessful && resp.body()?.status == "success") {
                    units = resp.body()!!.body.types
                        .flatMap { it.units }
                }
            }
    }

    fun onUnitPriceChange(v: String) {
        if (v.matches(Regex("^\\d*\\.?\\d*\$")))
            unitPrice = v
        priceError = null
        validateAndCalculateProfit()

    }

    fun onUnitCostChange(v: String)  {
        if (v.matches(Regex("^\\d*\\.?\\d*\$")))
            unitCost = v
        costError = null
        validateAndCalculateProfit()

    }

    private fun validateAndCalculateProfit() {
        if (unitCost.isNotBlank() && unitPrice.isNotBlank()) {
            val cost = unitCost.toDoubleOrNull() ?: 0.0
            val price = unitPrice.toDoubleOrNull() ?: 0.0

            priceError = if (price <= cost) {
                profitPercentage = null
                "El precio debe ser mayor que el costo"
            } else {

                profitPercentage = if (cost > 0) ((price - cost) / cost) * 100 else 0.0
                null
            }
        } else {
            profitPercentage = null
        }
    }

    fun getFormattedProfitPercentage(): String {
        return profitPercentage?.let { "%.2f%%".format(it) } ?: "--"
    }

    fun onQuantityChange(v: String)  {
        if (v.matches(Regex("^\\d*\\.?\\d*\$")))
            quantity = v
    }

    fun onBarCodeChange(v: String)   {
        barCode = v
    }

    fun onImageSelected(b: Bitmap?) {
        imageBitmap = b
        imageError = null
        imageChanged = true        // marcamos cambio
        Log.d(TAG, "onImageSelected(): bitmap=${b?.width}x${b?.height}, imageChanged=$imageChanged")

    }

    fun onClearImage() {
        imageBitmap = null
        imageChanged = true        // marcamos borrar
        Log.d(TAG, "onClearImage(): bitmap cleared, imageChanged=$imageChanged")

    }

    fun onCategorySelected(id: Int)  {
        selectedCategoryId = id
        categoryError = null
    }

    fun onUnitSelected(id: Int)      {
        selectedUnitId = id
        unitError = null
    }

    fun clearResult() {
        resultMessage = null
    }

    val canUpdate: Boolean
        get() {
            val o = orig ?: return false
            val changed =  name.trim()     != o.name
                    || description.trim() != o.description.orEmpty()
                    || unitPrice            != o.unitPrice
                    || unitCost             != o.unitCost
                    || quantity             != o.quantity
                    || barCode.trim()       != o.barcode.orEmpty()
                    || selectedCategoryId   != o.categoryId
                    || selectedUnitId       != o.unitOfMeasureId
                    || imageChanged
            val valid   = name.isNotBlank()
                    && unitPrice.isNotBlank()
                    && unitCost.isNotBlank()
                    && priceError == null
                    && selectedCategoryId != null
                    && selectedUnitId != null
            val result = changed && valid && !isLoading
            Log.d(TAG, "canUpdate? changed=$changed valid=$valid isLoading=$isLoading => $result")
            return result
        }

    fun onUpdateClick() = viewModelScope.launch {
        if (!canUpdate) return@launch
        isLoading = true

        fun rb(s: String) = s
            .trim()
            .toRequestBody("text/plain".toMediaType())

        val fields = mapOf(
            "name"            to rb(name),
            "description"     to rb(description.ifEmpty { "" }),
            "unitPrice"       to rb(unitPrice),
            "unitCost"        to rb(unitCost),
            "quantity"        to rb(quantity.ifBlank { "0" }),
            "barcode"         to rb(barCode.ifEmpty { "" }),
            "categoryId"      to rb(selectedCategoryId!!.toString()),
            "unitOfMeasureId" to rb(selectedUnitId!!.toString())
        )

        val imgPart = if (imageChanged) imageBitmap?.toCompressedPart() else null

        try {
            val resp = RetrofitClient.api
                .updateProductWithImage(productId, fields, imgPart)

            if (resp.isSuccessful && resp.body()?.status == "success") {
                resultMessage = "success"
            } else {
                val errorJson = resp.errorBody()?.string()
                val error = Gson().fromJson(errorJson, LoginResponse::class.java)
                resultMessage = error?.message ?: "Error desconocido"
                Log.e("NewCategoryVM", "HTTP ${resp.code()} - ${resp.errorBody()?.string()}")
            }
        } catch (e: SocketTimeoutException) {
            resultMessage = "El servidor tardó demasiado en responder."
        } catch (e: Exception) {
            resultMessage = "Error de red: ${e.localizedMessage}"
        } finally {
            isLoading = false
        }
    }

    private fun Bitmap.toCompressedPart(
        fieldName: String = "image",
        maxWidth: Int = 1024,
        quality: Int = 80
    ): MultipartBody.Part {
        val scale = maxWidth / width.toFloat()
        val scaled = Bitmap.createScaledBitmap(this, maxWidth, (height * scale).toInt(), true)
        val bos = ByteArrayOutputStream().apply {
            scaled.compress(Bitmap.CompressFormat.JPEG, quality, this)
        }
        return bos.toByteArray()
            .toRequestBody("image/jpeg".toMediaType())
            .let { body ->
                MultipartBody.Part.createFormData(fieldName, "product.jpg", body)
            }
    }

    fun onDeleteClick(onSuccess: () -> Unit) = viewModelScope.launch {
        isLoading = true
        val resp = RetrofitClient.api.deleteProduct(productId)
        try {
        if (resp.isSuccessful && resp.body()?.status == "success") {
            onSuccess()
        } else {
            val errorJson = resp.errorBody()?.string()
            val error = Gson().fromJson(errorJson, LoginResponse::class.java)
            resultMessage = error?.message ?: "Error desconocido"
            Log.e("NewCategoryVM", "HTTP ${resp.code()} - ${resp.errorBody()?.string()}")

        }
        } catch (e: SocketTimeoutException) {
            resultMessage = "El servidor tardó demasiado en responder. Intenta de nuevo."
        } catch (e: IOException) {
            resultMessage = "Error de red: ${e.localizedMessage}"
        } catch (e: Exception) {
            resultMessage = "Error inesperado: ${e.localizedMessage}"
        } finally {
            isLoading = false
        }
    }
}
