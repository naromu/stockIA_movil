package com.example.stockia.viewmodel.clients

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockia.model.Client
import kotlinx.coroutines.launch
import java.io.IOException


class ClientViewModel : ViewModel() {

    // Lista completa de clientes
    var clients by mutableStateOf<List<Client>>(emptyList())
        private set

    // Lista filtrada por búsqueda
    var filteredClients by mutableStateOf<List<Client>>(emptyList())
        private set

    // Campo de búsqueda
    var searchClient by mutableStateOf("")
        private set

    // Estado de carga
    var isLoading by mutableStateOf(false)
        private set

    // Mensaje de resultado (error o éxito)
    var resultMessage by mutableStateOf<String?>(null)
        private set

    init {
        loadClients()
    }

    fun onSearchClientChange(newValue: String) {
        searchClient = newValue
        filterClients()
    }

    private fun filterClients() {
        filteredClients = if (searchClient.isBlank()) {
            clients
        } else {
            clients.filter {
                it.name.contains(searchClient, ignoreCase = true)
            }
        }
    }

    fun loadClients() {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = RetrofitClient.api.getClients()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.status == "success") {
                        clients = body.data
                        filterClients()
                        resultMessage = null
                        Log.d("ClientVM", "Clientes cargados: ${clients.size}")
                    } else {
                        resultMessage = body?.message ?: "Respuesta inesperada del servidor"
                    }
                } else {
                    resultMessage = "Error ${response.code()} al obtener clientes"
                }
            } catch (e: IOException) {
                resultMessage = "Error de red"
            }
            catch (e: Exception) {
                resultMessage = e.localizedMessage ?: "Error desconocido al cargar clientes"
            }
            isLoading = false
        }
    }

    fun clearResultMessage() {
        resultMessage = null
    }
}
