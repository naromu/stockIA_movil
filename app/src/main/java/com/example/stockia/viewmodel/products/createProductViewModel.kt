package com.example.stockia.viewmodel.products

import UnitOfMeasure
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockia.model.Category
import com.example.stockia.model.CreateCategoryRequest
import com.example.stockia.model.CreateCategoryResponse
import com.example.stockia.model.LoginResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.SocketTimeoutException
class CreateProductViewModel : ViewModel() {

    var name by mutableStateOf("")
        private set

    var description by mutableStateOf("")
        private set

    var unitPrice by mutableStateOf("")
        private set

    var unitCost by mutableStateOf("")
        private set

    var quantity by mutableStateOf("")
        private set

    var barCode by mutableStateOf("")
        private set

    var imageBitmap by mutableStateOf<Bitmap?>(null)
        private set

    var categories by mutableStateOf<List<Category>>(emptyList())
        private set

    var units by mutableStateOf<List<UnitOfMeasure>>(emptyList())
        private set

    var selectedCategoryId by mutableStateOf<Int?>(null)
        private set

    var selectedUnitId by mutableStateOf<Int?>(null)
        private set

    var nameError by mutableStateOf<String?>(null)
        private set

    var priceError by mutableStateOf<String?>(null)
        private set

    var costError by mutableStateOf<String?>(null)
        private set

    var categoryError by mutableStateOf<String?>(null)
        private set

    var unitError by mutableStateOf<String?>(null)
        private set

    var imageError by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var resultMessage by mutableStateOf<String?>(null)
        private set

    var profitPercentage by mutableStateOf<Double?>(null)
        private set

    val isFormValid: Boolean
        get() = name.isNotBlank()
                && unitPrice.isNotBlank()
                && unitCost.isNotBlank()
                && selectedCategoryId !=null
                && selectedUnitId !=null
                && priceError ==null

    init {
        loadCategories()
        loadMeasurements()
    }

    fun onNameChange(v: String)        {
        name=v; nameError=null
    }

    fun onDescriptionChange(v:String)  {
        description=v
    }

    fun onUnitPriceChange(v: String) {
        if (v.matches(Regex("^\\d*\\.?\\d*$"))) {
            unitPrice = v
            priceError = null
            validateAndCalculateProfit()
        }
    }

    fun onUnitCostChange(v: String) {
        if (v.matches(Regex("^\\d*\\.?\\d*$"))) {
            unitCost = v
            costError = null
            validateAndCalculateProfit()

        }
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

    fun onQuantityChange(v:String)     {
        if (v.matches(Regex("^\\d*\\.?\\d*\$")))
            quantity=v
    }

    fun onBarCodeChange(v:String)      {
        barCode=v
    }

    fun onImageSelected(b:Bitmap?)     {
        imageBitmap=b
        imageError=null
    }

    fun onCategorySelected(id:Int)     {
        selectedCategoryId=id
        categoryError=null
    }

    fun onUnitSelected(id:Int)         {
        selectedUnitId=id
        unitError=null
    }

    private fun loadCategories() = viewModelScope.launch {
        runCatching { RetrofitClient.api.getCategories() }
            .onSuccess { resp ->
                if (resp.isSuccessful && resp.body()?.status=="success") {
                    categories = resp.body()!!.data
                }
            }
    }

    private fun loadMeasurements() = viewModelScope.launch {
        runCatching { RetrofitClient.api.getMeasurements() }
            .onSuccess { resp ->
                if (resp.isSuccessful && resp.body()?.status=="success") {
                    units = resp.body()!!.body.types.flatMap { it.units }
                }
            }
    }

    private fun validate(): Boolean {
        var ok = true
        if (name.isBlank())        { nameError="Requerido"; ok=false }
        if (unitPrice.isBlank())   { priceError="Requerido"; ok=false }
        if (unitCost.isBlank())    { costError="Requerido"; ok=false }
        if (selectedCategoryId==null) { categoryError="Requerido"; ok=false }
        if (selectedUnitId==null)     { unitError="Requerido"; ok=false }

        if (unitPrice.isNotBlank() && unitCost.isNotBlank()) {
            val price = unitPrice.toDoubleOrNull() ?: 0.0
            val cost = unitCost.toDoubleOrNull() ?: 0.0

            if (price <= cost) {
                priceError = "El precio debe ser mayor que el costo"
                ok = false
            }
        }

        return ok
    }

    fun Bitmap.toCompressedPart(
        fieldName: String = "image",
        maxWidth: Int = 256,
        quality: Int = 50
    ): MultipartBody.Part {
        val ratio = minOf(1f, maxWidth.toFloat() / width)
        val targetWidth  = (width  * ratio).toInt()
        val targetHeight = (height * ratio).toInt()

        val scaledBmp = if (ratio < 1f) {
            Bitmap.createScaledBitmap(this, targetWidth, targetHeight, true)
        } else {
            this
        }

        val bos = ByteArrayOutputStream().apply {
            scaledBmp.compress(Bitmap.CompressFormat.JPEG, quality, this)
        }
        val bytes = bos.toByteArray()

        return MultipartBody.Part.createFormData(
            fieldName,
            "product.jpg",
            bytes.toRequestBody("image/jpeg".toMediaType())
        )
    }


    fun onCreateClick() = viewModelScope.launch {
        if (!validate()) return@launch
        isLoading = true

        Log.d("CreateProductVM", "onCreateClick(): name='$name', unitPrice='$unitPrice', categoryId=$selectedCategoryId, unitId=$selectedUnitId, hasImage=${imageBitmap != null}")

        fun rb(s: String) = s.toRequestBody("text/plain".toMediaType())
        val partName          = rb(name.trim())
        val partDescription   = rb(description.trim())
        val partUnitPrice     = rb(unitPrice)
        val partUnitCost      = rb(unitCost)
        val partQuantity      = rb(quantity.ifBlank { "0" })
        val partBarcode       = rb(barCode.ifBlank { "" })
        val partCategoryId    = rb(selectedCategoryId!!.toString())
        val partUnitOfMeasure = rb(selectedUnitId!!.toString())

        val imgPart: MultipartBody.Part? = imageBitmap
            ?.toCompressedPart(fieldName = "image", maxWidth = 256, quality = 80)
            .also { part ->
                Log.d("CreateProductVM", "imgPart = ${if (part != null) "READY (size=${part.body.contentLength()})" else "NULL (no se enviará)"}")
            }

        try {
            val resp = RetrofitClient.api.createProduct(
                partName,
                partDescription,
                partUnitPrice,
                partUnitCost,
                partQuantity,
                partBarcode,
                partCategoryId,
                partUnitOfMeasure,
                imgPart
            )

            if (resp.isSuccessful && resp.body()?.status == "success") {
                resultMessage = "success"
                Log.d("CreateProductVM", "Producto creado OK")
            } else {
                val errorJson = resp.errorBody()?.string()
                val error = Gson().fromJson(errorJson, LoginResponse::class.java)
                resultMessage = error?.message ?: "Error desconocido"
                Log.e("NewCategoryVM", "HTTP ${resp.code()} - ${resp.errorBody()?.string()}")

            }
        } catch (e: SocketTimeoutException) {
            resultMessage = "El servidor tardó demasiado en responder. Intenta de nuevo."
            Log.d("CreateProductVM", "Timeout", e)
        } catch (e: IOException) {
            resultMessage = "Error de red: ${e.localizedMessage}"
            Log.d("CreateProductVM", "IO error", e)
        } catch (e: Exception) {
            resultMessage = "Error inesperado: ${e.localizedMessage}"
            Log.d("CreateProductVM", "Unexpected error", e)
        } finally {
            isLoading = false
        }
    }
}


