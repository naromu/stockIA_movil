package com.example.stockia.viewmodel.salesReturn

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockia.model.*
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

data class DevolucionItem(
    val productId: Int,
    val productName: String,
    val productDescription: String,
    val maxQuantity: Int,
    var quantity: Int,
    var statusId: Int
)

class FinalizeSalesReturnViewModel : ViewModel() {

    var items by mutableStateOf<List<DevolucionItem>>(emptyList())
        private set

    var notes by mutableStateOf("")
        private set

    var isSuccess by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun loadProductsFromOrder(orderId: Int, selectedProductIds: List<Int>) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.getSalesOrderById(orderId)
                if (response.isSuccessful) {
                    val orderDetail = response.body()?.data
                    val filtered = orderDetail?.products?.filter { selectedProductIds.contains(it.product_id) } ?: emptyList()

                    items = filtered.map {
                        DevolucionItem(
                            productId = it.product_id,
                            productName = it.product_name,
                            productDescription = it.product_description,
                            maxQuantity = it.quantity,
                            quantity = it.quantity, // inicial igual a lo comprado
                            statusId = 9 // En revisión
                        )
                    }
                } else {
                    errorMessage = "Error ${response.code()} al cargar productos"
                }
            } catch (e: Exception) {
                errorMessage = e.localizedMessage ?: "Error desconocido"
                Log.e("FinalizeSalesReturnVM", "Error al cargar orden", e)
            }
        }
    }

    fun updateQuantity(productId: Int, newQuantity: Int) {
        items = items.map {
            if (it.productId == productId) it.copy(quantity = newQuantity) else it
        }
    }

    fun updateStatus(productId: Int, newStatusId: Int) {
        items = items.map {
            if (it.productId == productId) it.copy(statusId = newStatusId) else it
        }
    }

    fun updateNotes(newValue: String) {
        notes = newValue
    }

    fun createSalesReturn(orderId: Int) {
        viewModelScope.launch {
            try {
                // Validación: no permitir más cantidad de la comprada
                if (items.any { it.quantity > it.maxQuantity }) {
                    errorMessage = "No puedes devolver más cantidad de la comprada"
                    return@launch
                }

                val request = CreateSalesReturnRequest(
                    salesOrderId = orderId,
                    returnDate = ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                    notes = notes,
                    items = items.map {
                        SalesReturnItemRequest(
                            productId = it.productId,
                            quantity = it.quantity,
                            statusId = it.statusId
                        )
                    }
                )

                val response = RetrofitClient.api.createSalesReturn(request)
                if (response.isSuccessful) {
                    isSuccess = true
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessageFromServer = try {
                        val errorJson = Gson().fromJson(errorBody, Map::class.java)
                        errorJson["message"] as? String ?: "Error desconocido del servidor"
                    } catch (e: Exception) {
                        "Error ${response.code()} al crear devolución"
                    }
                    errorMessage = errorMessageFromServer
                }

            } catch (e: Exception) {
                errorMessage = e.localizedMessage ?: "Error desconocido"
                Log.e("FinalizeSalesReturnVM", "Error al enviar devolución", e)
            }
        }
    }

    fun clearError() {
        errorMessage = null
    }
}
