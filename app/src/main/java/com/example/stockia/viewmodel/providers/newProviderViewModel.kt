package com.example.stockia.viewmodel.providers

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockia.model.CreateProviderRequest
import kotlinx.coroutines.launch

class NewProviderViewModel : ViewModel() {

    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var phone by mutableStateOf("")
    var address by mutableStateOf("")

    var resultMessage by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    val isFormValid: Boolean
        get() = name.isNotBlank() && email.isNotBlank() && phone.isNotBlank()

    fun clearResultMessage() {
        resultMessage = null
    }

    fun createProvider(onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            try {
                val request = CreateProviderRequest(name, email, phone, address)
                val response = RetrofitClient.api.createProvider(request)
                if (response.isSuccessful && response.body()?.status == "success") {
                    resultMessage = "Proveedor creado con éxito"
                    onSuccess()
                } else {
                    resultMessage = response.body()?.message ?: "Error al crear proveedor"
                }
            } catch (e: Exception) {
                resultMessage = e.localizedMessage ?: "Error inesperado"
            }
            isLoading = false
        }
    }
}
