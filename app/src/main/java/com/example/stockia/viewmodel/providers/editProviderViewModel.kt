package com.example.stockia.viewmodel.providers

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockia.model.UpdateProviderRequest
import kotlinx.coroutines.launch
import org.json.JSONObject

class EditProviderViewModel : ViewModel() {

    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var phone by mutableStateOf("")
    var address by mutableStateOf("")

    var isLoading by mutableStateOf(false)
        private set

    var resultMessage by mutableStateOf<String?>(null)
        private set

    val isFormValid: Boolean
        get() = name.isNotBlank() && email.isNotBlank() && phone.isNotBlank()

    fun clearResultMessage() {
        resultMessage = null
    }

    fun loadProvider(id: Int) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = RetrofitClient.api.getProviderById(id)
                if (response.isSuccessful && response.body()?.status == "success") {
                    response.body()?.data?.let {
                        name = it.name
                        email = it.email
                        phone = it.phone
                        address = it.address
                    }
                } else {
                    resultMessage = response.body()?.message ?: "Error al obtener proveedor"
                }
            } catch (e: Exception) {
                resultMessage = e.localizedMessage ?: "Error inesperado"
            }
            isLoading = false
        }
    }

    fun updateProvider(id: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            try {
                val request = UpdateProviderRequest(name, email, phone, address)
                val response = RetrofitClient.api.updateProvider(id, request)
                if (response.isSuccessful && response.body()?.status == "success") {
                    resultMessage = "Proveedor actualizado"
                    onSuccess()
                } else {
                    resultMessage = response.body()?.message ?: "Error al actualizar proveedor"
                }
            } catch (e: Exception) {
                resultMessage = e.localizedMessage ?: "Error inesperado"
            }
            isLoading = false
        }
    }

    fun deleteProvider(id: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = RetrofitClient.api.deleteProvider(id)
                if (response.isSuccessful && response.body()?.status == "success") {
                    resultMessage = "Proveedor eliminado"
                    onSuccess()
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = try {
                        JSONObject(errorBody).getString("message")
                    } catch (e: Exception) {
                        "Error ${response.code()} al eliminar"
                    }
                    resultMessage = errorMessage
                }
            } catch (e: Exception) {
                resultMessage = e.localizedMessage ?: "Error inesperado"
            }
            isLoading = false
        }
    }
}
