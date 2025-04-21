package com.example.stockia.viewmodel.clients

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockia.model.Client
import com.example.stockia.model.GetClientResponse
import com.example.stockia.model.UpdateClientRequest
import kotlinx.coroutines.launch

class EditClientViewModel : ViewModel() {

    var id by mutableStateOf<Int?>(null)
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

    fun loadClient(clientId: Int) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = RetrofitClient.api.getClientById(clientId)
                if (response.isSuccessful) {
                    val body = response.body()
                    val client = body?.data
                    if (client != null) {
                        id = client.id
                        name = client.name
                        email = client.email
                        phone = client.phone
                        address = client.address
                        resultMessage = null
                    } else {
                        resultMessage = "Cliente no encontrado"
                    }
                } else {
                    resultMessage = "Error ${response.code()} al obtener cliente"
                }
            } catch (e: Exception) {
                resultMessage = e.localizedMessage ?: "Error desconocido"
            }
            isLoading = false
        }
    }

    fun updateClient(onSuccess: () -> Unit) {
        val clientId = id ?: return
        val request = UpdateClientRequest(name, email, phone, address)

        viewModelScope.launch {
            isLoading = true
            try {
                val response = RetrofitClient.api.updateClient(clientId, request)
                if (response.isSuccessful) {
                    resultMessage = "Cliente actualizado"
                    onSuccess()
                } else {
                    resultMessage = "Error ${response.code()} al actualizar"
                }
            } catch (e: Exception) {
                resultMessage = e.localizedMessage ?: "Error desconocido"
            }
            isLoading = false
        }
    }

    fun deleteClient(onSuccess: () -> Unit) {
        val clientId = id ?: return

        viewModelScope.launch {
            isLoading = true
            try {
                val response = RetrofitClient.api.deleteClient(clientId)
                if (response.isSuccessful) {
                    resultMessage = "Cliente eliminado"
                    onSuccess()
                } else {
                    resultMessage = "Error ${response.code()} al eliminar"
                }
            } catch (e: Exception) {
                resultMessage = e.localizedMessage ?: "Error desconocido"
            }
            isLoading = false
        }
    }

    fun clearResultMessage() {
        resultMessage = null
    }
}
