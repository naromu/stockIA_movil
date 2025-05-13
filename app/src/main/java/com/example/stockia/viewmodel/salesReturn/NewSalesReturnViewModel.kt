package com.example.stockia.viewmodel.salesReturn

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockia.model.SalesOrder
import com.example.stockia.model.SalesOrdersListResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewSalesReturnViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(NewSalesReturnUiState())
    val uiState: StateFlow<NewSalesReturnUiState> = _uiState

    init {
        loadConfirmedSalesOrders()
    }

    private fun loadConfirmedSalesOrders() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.getSalesOrders()
                if (response.isSuccessful) {
                    val allOrders = response.body()?.data ?: emptyList()
                    val confirmed = allOrders.filter { it.status_id == 2 }
                    _uiState.update { it.copy(confirmedSalesOrders = confirmed) }
                } else {
                    Log.e("NewSalesReturnVM", "Error ${response.code()} al obtener órdenes")
                }
            } catch (e: Exception) {
                Log.e("NewSalesReturnVM", "Excepción: ${e.localizedMessage}")
            }
        }
    }
}

data class NewSalesReturnUiState(
    val confirmedSalesOrders: List<SalesOrder> = emptyList()
)
