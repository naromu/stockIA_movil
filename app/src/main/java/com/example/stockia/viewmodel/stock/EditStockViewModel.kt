package com.example.stockia.viewmodel.stock

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockia.model.UpdateStockRequest
import com.example.stockia.model.UpdateStockResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch

class EditStockViewModel : ViewModel() {

    var newStock by mutableStateOf(0)
        private set

    var originalStock by mutableStateOf(0)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var isEditing by mutableStateOf(false)
        private set

    var resultMessage by mutableStateOf<String?>(null)
        private set

    val isFormValid: Boolean
        get() = newStock != originalStock && newStock >= 0

    fun onStockChange(value: String) {
        newStock = value.toIntOrNull() ?: 0
    }

    fun setInitialStock(id: Int, stock: Int) {
        originalStock = stock
        newStock = stock
    }

    fun clearResultMessage() {
        resultMessage = null
    }

    fun updateStock(id: Int) {
        viewModelScope.launch {
            isLoading = true
            isEditing = true
            try {
                val req = UpdateStockRequest(quantity = newStock)
                val resp = RetrofitClient.api.updateStock(id, req)
                if (resp.isSuccessful) {
                    resultMessage = "success"
                } else {
                    val errJson = resp.errorBody()?.string()
                    val err = Gson().fromJson(errJson, UpdateStockResponse::class.java)
                    resultMessage = err.message ?: "Error ${resp.code()}"
                }
            } catch (e: Exception) {
                Log.e("EditStockVM", "Error actualizando", e)
                resultMessage = "Error de red"
            }
            isLoading = false
            isEditing = false
        }
    }
}
