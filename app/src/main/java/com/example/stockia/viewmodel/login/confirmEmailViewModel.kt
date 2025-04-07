package com.example.stockia.viewmodel.login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockia.model.ConfirmEmailRequest
import com.example.stockia.model.ConfirmEmailResponse
import com.example.stockia.model.RetrofitClient
import kotlinx.coroutines.launch

class ConfirmEmailViewModel : ViewModel() {

    var code by mutableStateOf("")
        private set

    var resultMessage by mutableStateOf<String?>(null)
        private set

    val isFormValid: Boolean
        get() = code.isNotBlank()

    var isLoading by mutableStateOf(false)
        private set

    fun clearResultMessage() {
        resultMessage = null
    }


    fun onCodeChange(newValue: String) {
        code = newValue
    }

    fun onRegisterClick() {
        viewModelScope.launch {
            isLoading = true
            try {
                Log.d("ConfirmViewModel", "Boton oprimido")

                val request = ConfirmEmailRequest(token = code)

                val response = RetrofitClient.api.confirmEmail(request)

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.status == "success") {
                        Log.d("ConfirmViewModel", "success")

                        resultMessage = "success"
                    } else {
                        Log.d("ConfirmViewModel", "inesperado")

                        resultMessage = body?.message ?: "Error inesperado"
                    }
                } else {
                    val errorJson = response.errorBody()?.string()
                    val gson = com.google.gson.Gson()
                    val errorResponse = gson.fromJson(errorJson, ConfirmEmailResponse::class.java)
                    resultMessage = errorResponse.message ?: "Error al confirmar"
                }
            } catch (e: Exception) {
                resultMessage = e.message ?: "Error desconocido"
            }
        }
    }

}