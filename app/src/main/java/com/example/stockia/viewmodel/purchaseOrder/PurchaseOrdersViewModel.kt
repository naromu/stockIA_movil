package com.example.stockia.viewmodel.purchaseOrder

import CreatePurchaseOrderRequest
import PurchaseOrder
import PurchaseOrderItemRequest
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockia.model.*
import com.google.gson.Gson
import kotlinx.coroutines.launch

class PurchaseOrdersViewModel : ViewModel() {

    var purchaseOrders by mutableStateOf<List<PurchaseOrder>>(emptyList())
        private set

    var searchQuery by mutableStateOf("")
        private set

    var resultMessage by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    val filteredOrders: List<PurchaseOrder>
        get() = if (searchQuery.isBlank()) purchaseOrders
        else purchaseOrders.filter {
            it.id.toString().contains(searchQuery, ignoreCase = true) ||
                    it.supplier_name?.contains(searchQuery, ignoreCase = true) == true ||
                    it.status_name?.contains(searchQuery, ignoreCase = true) == true
        }



    fun onSearchQueryChange(newValue: String) {
        searchQuery = newValue
    }

    fun clearResultMessage() {
        resultMessage = null
    }

    fun loadPurchaseOrders() {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = RetrofitClient.api.getPurchaseOrders()
                if (response.isSuccessful && response.body()?.status == "success") {
                    purchaseOrders = response.body()!!.data.sortedByDescending { it.purchase_order_date }
                    resultMessage = null
                } else {
                    resultMessage = response.body()?.message ?: "Error al obtener órdenes"
                }
            } catch (e: Exception) {
                resultMessage = e.localizedMessage ?: "Error desconocido"
            }
            isLoading = false
        }
    }

    fun confirmOrder(orderId: Int) {
        viewModelScope.launch {
            isLoading = true
            try {
                // 1. Obtener detalle para reusar campos
                val detailResp = RetrofitClient.api.getPurchaseOrderById(orderId)
                val detail = detailResp.body()?.data
                if (!detailResp.isSuccessful || detail == null) {
                    resultMessage = "No se pudo obtener detalle"
                    return@launch
                }
                val items = detail.products.mapNotNull {
                    val unitCost = it.unit_cost.toDoubleOrNull()
                    if (it.product_id != null && it.quantity > 0 && unitCost != null && unitCost > 0.0) {
                        PurchaseOrderItemRequest(
                            productId = it.product_id,
                            quantity = it.quantity,
                            unitCost = unitCost
                        )
                    } else {
                        Log.d("PurchaseOrdersVM", "Producto inválido filtrado: id=${it.product_id}, quantity=${it.quantity}, unitCost=${it.unit_cost}")
                        null
                    }
                }



                // 2. Armar request con statusId = 4 (confirmed)
                val request = CreatePurchaseOrderRequest(
                    supplierId = detail.supplier_id,
                    statusId = 4,
                    notes = detail.notes ?: "",
                    purchaseOrderDate = detail.purchase_order_date,
                    items = items
                )



                // 3. Enviar PUT
                val updateResp = RetrofitClient.api.updatePurchaseOrder(orderId, request)

                if (updateResp.isSuccessful) {
                    // Leer body en caso 2xx
                    val body = updateResp.body()
                    if (body?.status == "success") {
                        resultMessage = "Orden confirmada"
                        loadPurchaseOrders()
                    } else {
                        // El servidor devolvió status != success
                        resultMessage = body?.message ?: "Error al confirmar"
                    }
                } else {
                    // Leer errorBody JSON
                    val errorJson = updateResp.errorBody()?.string()
                    val errorResp = Gson().fromJson(errorJson, GenericResponse::class.java)
                    resultMessage = errorResp?.message ?: "Error al confirmar"
                    Log.d("PurchaseOrdersVM", resultMessage!!)

                }

            } catch (e: Exception) {
                Log.d("PurchaseOrdersVM", "Error: ${e.message}")
                resultMessage = e.message ?: "Error inesperado"
            } finally {
                isLoading = false
            }
        }
    }

}
