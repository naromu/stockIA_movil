package com.example.stockia.viewmodel.purchaseReturn

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockia.model.PurchaseReturn
import kotlinx.coroutines.launch

class PurchaseReturnsViewModel : ViewModel() {

    var searchQuery by mutableStateOf("")
        private set

    fun onSearchQueryChange(newValue: String) {
        searchQuery = newValue
    }

    var allReturns by mutableStateOf<List<PurchaseReturn>>(emptyList())
        private set

    var searchText by mutableStateOf("")
        private set

    var resultMessage by mutableStateOf<String?>(null)
        private set

    val filteredReturns: List<PurchaseReturn>
        get() = allReturns.filter {
            it.notes.contains(searchText, ignoreCase = true) ||
                    it.purchase_order_id.toString().contains(searchText)
        }

    fun loadReturns() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.getPurchaseReturns()
                if (response.isSuccessful) {
                    allReturns = response.body()?.data ?: emptyList()
                } else {
                    resultMessage = "Error ${response.code()} al obtener devoluciones"
                }
            } catch (e: Exception) {
                resultMessage = e.localizedMessage ?: "Error de red"
            }
        }
    }

    fun onSearchChange(newValue: String) {
        searchText = newValue
    }

    fun clearResultMessage() {
        resultMessage = null
    }
}
