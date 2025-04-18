package com.example.stockia.viewmodel.categories

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockia.model.Category
import com.example.stockia.model.CategoriesResponse
import kotlinx.coroutines.launch

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
        get() = true  // aquí no hay validación extra

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
                    resultMessage = "Error ${response.code()}: al obtener categorías"
                }
            } catch (e: Exception) {
                resultMessage = e.localizedMessage ?: "Error desconocido"
            }
            isLoading = false
        }
    }
}
