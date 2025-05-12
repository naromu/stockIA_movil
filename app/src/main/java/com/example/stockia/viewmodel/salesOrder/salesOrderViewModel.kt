package com.example.stockia.viewmodel.salesOrder

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockia.model.CreateSalesOrderRequest
import com.example.stockia.model.SalesOrder
import com.example.stockia.model.SalesOrderItemRequest
import com.example.stockia.model.SalesOrdersListResponse
import kotlinx.coroutines.launch
import android.util.Log


class SalesOrdersViewModel : ViewModel() {

    var salesOrders by mutableStateOf<List<SalesOrder>>(emptyList())
        private set

    var searchQuery by mutableStateOf("")
        private set

    var resultMessage by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    val filteredOrders: List<SalesOrder>
        get() = if (searchQuery.isBlank()) salesOrders
        else salesOrders.filter {
            it.customer_name?.contains(searchQuery, ignoreCase = true) == true ||
                    it.status_name?.contains(searchQuery, ignoreCase = true) == true
        }

    fun onSearchQueryChange(newValue: String) {
        searchQuery = newValue
    }

    fun clearResultMessage() {
        resultMessage = null
    }

    fun updateResultMessage(message: String?) {
        resultMessage = message
    }


    fun loadSalesOrders() {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = RetrofitClient.api.getSalesOrders()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.status == "success") {
                        salesOrders = body.data
                        resultMessage = null
                    } else {
                        resultMessage = body?.message ?: "Respuesta inesperada"
                    }
                } else {
                    resultMessage = "Error ${response.code()} al obtener órdenes"
                }
            } catch (e: Exception) {
                resultMessage = e.localizedMessage ?: "Error desconocido"
            }
            isLoading = false
        }
    }

    fun confirmOrder(orderId: Int) {
        viewModelScope.launch {
            try {
                // 1. Obtener detalle de la orden
                val detailResponse = RetrofitClient.api.getSalesOrderById(orderId)
                val detail = detailResponse.body()?.data

                if (!detailResponse.isSuccessful || detail == null) {
                    resultMessage = "No se pudo obtener el detalle de la orden"
                    return@launch
                }

                // 2. Armar el request actualizado con statusId = 2 (Confirmada)
                val request = CreateSalesOrderRequest(
                    customerId = detail.customer_id,
                    statusId = 2, // Confirmada
                    notes = detail.notes,
                    salesOrderDate = detail.sales_order_date,
                    items = detail.products.map {
                        SalesOrderItemRequest(
                            productId = it.product_id,
                            quantity = it.quantity
                        )
                    }
                )

                // 3. Enviar PUT al backend
                val updateResponse = RetrofitClient.api.updateSalesOrder(orderId, request)

                if (updateResponse.isSuccessful) {
                    resultMessage = "Orden confirmada correctamente"
                    loadSalesOrders() // Recarga lista
                    Log.d("SalesOrdersViewModel", "Mensaje confirmado: $resultMessage")
                } else {
                    resultMessage = "Error al confirmar la orden"
                    Log.d("SalesOrdersViewModel", "Mensaje de error: $resultMessage")
                }

            } catch (e: Exception) {
                resultMessage = "Excepción: ${e.localizedMessage}"
                Log.d("SalesOrdersViewModel", "Excepción capturada: $resultMessage")
            }
        }
    }



}
