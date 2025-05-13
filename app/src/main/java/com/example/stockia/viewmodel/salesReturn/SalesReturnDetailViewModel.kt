package com.example.stockia.viewmodel.salesReturn

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockia.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.SocketTimeoutException

class SalesReturnDetailViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SalesReturnDetailUiState())
    val uiState: StateFlow<SalesReturnDetailUiState> = _uiState

    fun loadSalesReturn(id: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.getSalesReturnById(id)
                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d("SalesReturnVM", "Respuesta del backend: $body")
                    if (body?.status == "success") {
                        Log.d("SalesReturnVM", "Items recibidos: ${body.data.items}")
                        _uiState.update {
                            it.copy(
                                salesReturnId = body.data.id,
                                salesOrderId = body.data.sales_order_id,
                                returnDate = body.data.return_date,
                                notes = body.data.notes,
                                items = body.data.items,
                                updatedItems = body.data.items
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(errorMessage = body?.message ?: "Respuesta inesperada")
                        }
                    }
                } else {
                    _uiState.update {
                        it.copy(errorMessage = "Error ${response.code()} al obtener la devolución")
                    }
                }
            } catch (e: SocketTimeoutException) {
                _uiState.update {
                    it.copy(errorMessage = "El servidor tardó demasiado en responder. Intenta de nuevo.")
                }
            } catch (e: IOException) {
                _uiState.update {
                    it.copy(errorMessage = "Error de red: ${e.localizedMessage}")
                }
            } catch (e: Exception) {
                Log.e("SalesReturnVM", "Error al cargar devolución", e)
                _uiState.update {
                    it.copy(errorMessage = "Error inesperado: ${e.localizedMessage}")
                }
            }
        }
    }

    fun updateProductStatusLocally(productId: Int, newStatusId: Int) {
        _uiState.update { current ->
            val updated = current.updatedItems.map {
                if (it.product_id == productId) it.copy(status_id = newStatusId) else it
            }
            current.copy(updatedItems = updated)
        }
    }

    fun submitUpdatedSalesReturn() {
        viewModelScope.launch {
            try {
                val current = _uiState.value
                val request = UpdateSalesReturnRequest(
                    salesOrderId = current.salesOrderId,
                    returnDate = current.returnDate,
                    notes = current.notes,
                    items = current.updatedItems.map {
                        SalesReturnItemRequest(
                            productId = it.product_id,
                            quantity = it.quantity,
                            statusId = it.status_id
                        )
                    }
                )
                val response = RetrofitClient.api.updateSalesReturn(current.salesReturnId, request)
                if (response.isSuccessful) {
                    Log.d("SalesReturnVM", "Actualización exitosa")
                    loadSalesReturn(current.salesReturnId)
                } else {
                    _uiState.update {
                        it.copy(errorMessage = "Error al actualizar devolución: ${response.code()}")
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = "Error: ${e.localizedMessage}")
                }
            }
        }
    }

    fun updateSalesReturn(
        returnId: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val current = _uiState.value
                val request = UpdateSalesReturnRequest(
                    salesOrderId = current.salesOrderId,
                    returnDate = current.returnDate,
                    notes = current.notes,
                    items = current.updatedItems.map {
                        SalesReturnItemRequest(
                            productId = it.product_id,
                            quantity = it.quantity,
                            statusId = it.status_id
                        )
                    }
                )
                val response = RetrofitClient.api.updateSalesReturn(returnId, request)
                if (response.isSuccessful) {
                    Log.d("SalesReturnVM", "Devolución actualizada correctamente")
                    loadSalesReturn(returnId)
                    onSuccess()
                } else {
                    val message = "Error ${response.code()} al actualizar devolución"
                    Log.e("SalesReturnVM", message)
                    onError(message)
                }
            } catch (e: Exception) {
                val msg = "Error inesperado: ${e.localizedMessage}"
                Log.e("SalesReturnVM", msg)
                onError(msg)
            }
        }
    }

    fun selectProductForStatusChange(productId: Int?) {
        _uiState.update {
            it.copy(selectedProductId = productId)
        }
    }

    fun clearErrorMessage() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}

// UI State

data class SalesReturnDetailUiState(
    val salesReturnId: Int = 0,
    val salesOrderId: Int = 0,
    val returnDate: String = "",
    val notes: String = "",
    val items: List<SalesReturnItem> = emptyList(),
    val updatedItems: List<SalesReturnItem> = emptyList(),
    val selectedProductId: Int? = null,
    val errorMessage: String? = null
)
