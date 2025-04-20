package com.example.stockia.viewmodel.categories

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockia.model.Category
import com.example.stockia.model.CategoriesResponse
import com.example.stockia.model.LoginResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.SocketTimeoutException

class CategoriesViewModel : ViewModel() {

    var searchCategory by mutableStateOf("")
        private set

    var categories by mutableStateOf<List<Category>>(emptyList())
        private set

    var filteredCategories by mutableStateOf<List<Category>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var resultMessage by mutableStateOf<String?>(null)
        private set

    val isFormValid: Boolean
        get() = true

    init {
        loadCategories()
    }

    fun clearResultMessage() {
        resultMessage = null
    }

    fun onSearchCategoryChange(newValue: String) {
        searchCategory = newValue
        filterCategories()
    }

    private fun filterCategories() {
        filteredCategories = if (searchCategory.isBlank()) {
            categories
        } else {
            categories.filter {
                it.name.contains(searchCategory, ignoreCase = true)
            }
        }
    }

    fun loadCategories() {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = RetrofitClient.api.getCategories()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.status == "success") {
                        categories = body.data
                        filterCategories()
                        resultMessage = null
                        Log.d("CategoriesVM", "Categorías cargadas: ${categories.size}")
                    } else {
                        resultMessage = body?.message ?: "Respuesta inesperada"
                    }
                } else {
                    val errorJson = response.errorBody()?.string()
                    val error = Gson().fromJson(errorJson, LoginResponse::class.java)
                    resultMessage = error?.message ?: "Error desconocido"
                    Log.e("NewCategoryVM", "HTTP ${response.code()} - ${response.errorBody()?.string()}")

                }
            } catch (e: SocketTimeoutException) {
                resultMessage = "El servidor tardó demasiado en responder. Intenta de nuevo."
                Log.d("CreateProductVM", "Timeout", e)
            } catch (e: IOException) {
                resultMessage = "Error de red: ${e.localizedMessage}"
                Log.d("CreateProductVM", "IO error", e)
            } catch (e: Exception) {
                resultMessage = "Error inesperado: ${e.localizedMessage}"
                Log.d("CreateProductVM", "Unexpected error", e)
            } finally {
                isLoading = false
            }
        }
    }
}
