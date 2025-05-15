package com.example.stockia.viewmodel.purchaseReturn

import PurchaseOrderProduct
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockia.model.*
import kotlinx.coroutines.launch

class CompleteNewPurchaseReturnViewModel : ViewModel() {

    var products by mutableStateOf<List<PurchaseOrderProduct>>(emptyList())
        private set

    var selectedProducts by mutableStateOf<Set<Int>>(emptySet())
        private set

    var resultMessage by mutableStateOf<String?>(null)
        private set

    fun loadPurchaseOrder(orderId: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.getPurchaseOrderById(orderId)
                if (response.isSuccessful) {
                    products = response.body()?.data?.products ?: emptyList()
                    resultMessage = null
                } else {
                    resultMessage = "Error ${response.code()} al obtener la orden de compra"
                }
            } catch (e: Exception) {
                resultMessage = e.localizedMessage ?: "Error desconocido"
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
