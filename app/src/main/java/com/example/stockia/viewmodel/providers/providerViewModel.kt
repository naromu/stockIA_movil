package com.example.stockia.viewmodel.providers

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockia.model.Provider
import com.example.stockia.model.ProvidersResponse
import kotlinx.coroutines.launch
import java.io.IOException

class ProviderViewModel : ViewModel() {

    var searchProvider by mutableStateOf("")
        private set

    var providers by mutableStateOf<List<Provider>>(emptyList())
        private set

    var filteredProviders by mutableStateOf<List<Provider>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var resultMessage by mutableStateOf<String?>(null)
        private set

    val isFormValid: Boolean
        get() = true

    fun clearResultMessage() {
        resultMessage = null
    }

    fun onSearchProviderChange(newValue: String) {
        searchProvider = newValue
        filterProviders()
    }

    private fun filterProviders() {
        filteredProviders = if (searchProvider.isBlank()) {
            providers
        } else {
            providers.filter {
                it.name.contains(searchProvider, ignoreCase = true)
            }
        }
    }

    fun loadProviders() {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = RetrofitClient.api.getProviders()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.status == "success") {
                        providers = body.data
                        filterProviders()
                        resultMessage = null
                    } else {
                        resultMessage = body?.message ?: "Error desconocido"
                    }
                } else {
                    resultMessage = "Error ${response.code()}"
                }
            } catch (e: IOException) {
                resultMessage = "Error de red"
            }
            catch (e: Exception) {
                resultMessage = e.localizedMessage ?: "Fallo al cargar proveedores"
            }
            isLoading = false
        }
    }
}
