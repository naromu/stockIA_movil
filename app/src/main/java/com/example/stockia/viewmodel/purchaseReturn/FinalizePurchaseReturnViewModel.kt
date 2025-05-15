package com.example.stockia.viewmodel.purchaseReturn

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockia.model.*
import com.google.gson.Gson
import kotlinx.coroutines.launch
import PurchaseOrderProduct

class FinalizePurchaseReturnViewModel : ViewModel() {

    var products by mutableStateOf<List<PurchaseOrderProduct>>(emptyList())
        private set

    var quantities by mutableStateOf<Map<Int, Int>>(emptyMap())
        private set

    var notes by mutableStateOf("")
        private set

    var isSuccess by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun loadSelectedProducts(orderId: Int, selectedIds: List<Int>) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.getPurchaseOrderById(orderId)
                if (response.isSuccessful) {
                    val allProducts = response.body()?.data?.products ?: emptyList()
                    products = allProducts.filter { selectedIds.contains(it.product_id) }
                    // Inicializar cantidades en 1
                    quantities = products.associate { it.product_id to 1 }
                } else {
                    errorMessage = "Error ${response.code()} al obtener productos"
                }
            } catch (e: Exception) {
                errorMessage = e.localizedMessage ?: "Error desconocido"
            }
        }
    }

    fun changeQuantity(productId: Int, amount: Int) {
        val current = quantities[productId] ?: 1
        val newValue = (current + amount).coerceAtLeast(1)
        quantities = quantities.toMutableMap().apply {
            put(productId, newValue)
        }
    }

    fun onNotesChange(value: String) {
        notes = value
    }

    fun clearError() {
        errorMessage = null
    }

    fun submitReturn(orderId: Int) {
        viewModelScope.launch {
            try {
                val items = products.mapNotNull { product ->
                    val quantity = quantities[product.product_id] ?: return@mapNotNull null
                    PurchaseReturnItemRequest(productId = product.product_id, quantity = quantity)
                }

                val request = CreatePurchaseReturnRequest(
                    purchaseOrderId = orderId,
                    returnDate = getCurrentDateTime(),
                    notes = notes,
                    items = items
                )

                val response = RetrofitClient.api.createPurchaseReturn(request)
                if (response.isSuccessful) {
                    isSuccess = true
                } else {
                    val errorJson = response.errorBody()?.string()
                    val parsed = Gson().fromJson(errorJson, BaseResponse::class.java)
                    errorMessage = parsed.message ?: "Error del servidor"
                }
            } catch (e: Exception) {
                errorMessage = e.localizedMessage ?: "Error desconocido"
            }
        }
    }

    private fun getCurrentDateTime(): String {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", java.util.Locale.getDefault())
        sdf.timeZone = java.util.TimeZone.getTimeZone("UTC")
        return sdf.format(java.util.Date())
    }

}
