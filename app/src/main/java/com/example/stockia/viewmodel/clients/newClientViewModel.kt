package com.example.stockia.viewmodel.clients

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockia.model.CreateClientRequest
import kotlinx.coroutines.launch

class NewClientViewModel : ViewModel() {

    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var phone by mutableStateOf("")
    var address by mutableStateOf("")

    var isLoading by mutableStateOf(false)
        private set

    var resultMessage by mutableStateOf<String?>(null)
        private set

    val isFormValid: Boolean
        get() = name.isNotBlank() && email.isNotBlank() && phone.isNotBlank() && address.isNotBlank()

    fun createClient(onSuccess: () -> Unit) {
        val request = CreateClientRequest(name, email, phone, address)

        viewModelScope.launch {
            isLoading = true
            try {
                val response = RetrofitClient.api.createClient(request)
                if (response.isSuccessful) {
                    resultMessage = "Cliente creado con éxito"
                    clearForm()
                    onSuccess()
                } else {
                    resultMessage = "Error ${response.code()} al crear cliente"
                }
            } catch (e: Exception) {
                resultMessage = e.localizedMessage ?: "Error desconocido"
            }
            isLoading = false
        }
    }

    fun clearForm() {
        name = ""
        email = ""
        phone = ""
        address = ""
    }

    fun clearResultMessage() {
        resultMessage = null
    }
}
