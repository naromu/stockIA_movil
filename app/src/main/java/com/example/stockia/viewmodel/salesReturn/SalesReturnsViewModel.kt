package com.example.stockia.viewmodel.salesReturn

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockia.model.SalesReturn
import com.example.stockia.model.SalesReturnsResponse
import kotlinx.coroutines.launch
import java.io.IOException

class SalesReturnsViewModel : ViewModel() {

    var searchQuery by mutableStateOf("")
        private set

    var salesReturns by mutableStateOf<List<SalesReturn>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var resultMessage by mutableStateOf<String?>(null)
        private set

    val filteredReturns: List<SalesReturn>
        get() = if (searchQuery.isBlank()) salesReturns
        else salesReturns.filter {
            it.notes?.contains(searchQuery, ignoreCase = true) == true
                    || it.sales_order_id.toString().contains(searchQuery)
        }

    fun onSearchQueryChange(newValue: String) {
        searchQuery = newValue
    }

    fun clearResultMessage() {
        resultMessage = null
    }

    fun loadSalesReturns() {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = RetrofitClient.api.getSalesReturns()
                if (response.isSuccessful) {
                    salesReturns = response.body()?.data ?: emptyList()
                } else {
                    resultMessage = "Error ${response.code()}"
                }
            } catch (e: IOException) {
                resultMessage = "Error de red"
            }
            catch (e: Exception) {
                resultMessage = "Error: ${e.localizedMessage}"
            }
            isLoading = false
        }
    }
}
