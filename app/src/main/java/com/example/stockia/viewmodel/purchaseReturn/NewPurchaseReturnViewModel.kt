package com.example.stockia.viewmodel.purchaseReturn

import PurchaseOrder
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockia.model.*
import kotlinx.coroutines.launch

class NewPurchaseReturnViewModel : ViewModel() {

    var purchaseOrders by mutableStateOf<List<PurchaseOrder>>(emptyList())
        private set

    var resultMessage by mutableStateOf<String?>(null)
        private set

    fun loadPurchaseOrders() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.getPurchaseOrders()
                if (response.isSuccessful) {
                    val allOrders = response.body()?.data ?: emptyList()
                    purchaseOrders = allOrders.filter { it.status_id == 4 }
                    resultMessage = null
                } else {
                    resultMessage = "Error ${response.code()} al obtener órdenes de compra"
                }
            } catch (e: Exception) {
                resultMessage = e.localizedMessage ?: "Error desconocido"
            }
        }
    }

    fun clearResultMessage() {
        resultMessage = null
    }
}
