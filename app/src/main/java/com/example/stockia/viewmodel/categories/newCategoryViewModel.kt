package com.example.stockia.viewmodel.categories

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockia.model.CreateCategoryRequest
import com.example.stockia.model.CreateCategoryResponse
import com.example.stockia.model.LoginResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.Response

class NewCategoryViewModel : ViewModel() {

    var name by mutableStateOf("")
        private set

    var isLoading by mutableStateOf(false)
        private set

    var resultMessage by mutableStateOf<String?>(null)
        private set

    val isFormValid: Boolean
        get() = name.isNotBlank()

    fun onNameChange(newName: String) {
        name = newName
    }

    fun clearResultMessage() {
        resultMessage = null
    }

    fun onCreateClick() {
        if (!isFormValid) {
            resultMessage = "El nombre es requerido"
            return
        }
        viewModelScope.launch {
            isLoading = true
            try {
                val request = CreateCategoryRequest(name = name.trim())
                val response: Response<CreateCategoryResponse> =
                    RetrofitClient.api.createCategory(request)
                val body = response.body()

                if (response.isSuccessful) {
                    if (body?.status == "success") {
                        Log.d("NewCategoryVM", "Categoría creada: ${body.data.id}")
                        resultMessage = "success"

                    } else {
                        resultMessage = body?.message ?: "Error inesperado"
                        Log.w("NewCategoryVM", "API devolvió: ${body?.message}")
                    }
                } else {
                    val errorJson = response.errorBody()?.string()
                    val error = Gson().fromJson(errorJson, LoginResponse::class.java)
                    resultMessage = error?.message ?: "Error desconocido"
                    Log.e("NewCategoryVM", "HTTP ${response.code()} - ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                resultMessage = e.localizedMessage ?: "Error desconocido"
                Log.e("NewCategoryVM", "Exception creando categoría", e)
            }
            isLoading = false
        }
    }
}
