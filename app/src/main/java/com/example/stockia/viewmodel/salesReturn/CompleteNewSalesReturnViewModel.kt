package com.example.stockia.viewmodel.salesReturn

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockia.model.SalesOrderProduct
import com.example.stockia.model.SalesOrderDetailResponse
import kotlinx.coroutines.launch

class CompleteNewSalesReturnViewModel : ViewModel() {

    var products by mutableStateOf<List<SalesOrderProduct>>(emptyList())
        private set

    var selectedProducts by mutableStateOf<Set<Int>>(emptySet()) // product_id
        private set

    var resultMessage by mutableStateOf<String?>(null)
        private set

    fun loadSalesOrder(orderId: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.getSalesOrderById(orderId)
                if (response.isSuccessful) {
                    val orderDetail = response.body()?.data
                    products = orderDetail?.products ?: emptyList()
                    resultMessage = null
                } else {
                    resultMessage = "Error ${response.code()} al obtener la orden"
                }
            } catch (e: Exception) {
                resultMessage = e.localizedMessage ?: "Error desconocido"
                Log.e("CompleteNewSalesReturnVM", "Error al cargar orden", e)
            }
        }
    }

    fun toggleProductSelection(productId: Int) {
        selectedProducts = if (selectedProducts.contains(productId)) {
            selectedProducts - productId
        } else {
            selectedProducts + productId
        }
    }

    fun isProductSelected(productId: Int): Boolean {
        return selectedProducts.contains(productId)
    }

    fun clearResultMessage() {
        resultMessage = null
    }
}
