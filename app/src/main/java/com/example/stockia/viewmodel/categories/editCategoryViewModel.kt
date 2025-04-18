package com.example.stockia.viewmodel.categories

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockia.model.*
import com.google.gson.Gson
import kotlinx.coroutines.launch

class EditCategoryViewModel : ViewModel() {

    var name by mutableStateOf("")
        private set

    var originalName by mutableStateOf("")
        private set

    var isLoading by mutableStateOf(false)
        private set

    var isEditing by mutableStateOf(false)
        private set

    var nameError by mutableStateOf<String?>(null)
        private set

    var resultMessage by mutableStateOf<String?>(null)
        private set

    val isFormValid: Boolean
        get() = name.isNotBlank() && name != originalName

    fun onNameChange(newName: String) {
        name = newName
        nameError = if (name == originalName){
            "El nombre debe ser diferente al actual"
        } else if (name.isBlank()){
            "El nombre no puede estar vacio"
        }
        else null
    }


    fun clearResultMessage() {
        resultMessage = null
    }

    fun loadCategory(id: Int) {
        viewModelScope.launch {
            isLoading = true
            try {
                val resp = RetrofitClient.api.getCategoryById(id)
                if (resp.isSuccessful) {
                    resp.body()?.data?.let { category ->
                        name = category.name
                        originalName = category.name
                    }
                } else {
                    val errJson = resp.errorBody()?.string()
                    val err = Gson().fromJson(errJson, GetCategoryResponse::class.java)
                    resultMessage = err.message ?: "Error ${resp.code()}"
                }
            } catch (e: Exception) {
                Log.e("EditCategoryVM", "Error cargando", e)
                resultMessage = "Error de red"
            }
            isLoading = false
        }
    }

    fun onUpdateClick(id: Int) {
        if (name.isBlank()) {
            resultMessage = "El nombre no puede quedar vacío"
            return
        }
        viewModelScope.launch {
            isLoading = true
            isEditing = true
            try {
                val req = UpdateCategoryRequest(name = name.trim())
                val resp = RetrofitClient.api.updateCategory(id, req)
                if (resp.isSuccessful) {
                    resultMessage = "success"
                } else {
                    val errJson = resp.errorBody()?.string()
                    val err = Gson().fromJson(errJson, UpdateCategoryResponse::class.java)
                    resultMessage = err.message ?: "Error ${resp.code()}"
                }
            } catch (e: Exception) {
                Log.e("EditCategoryVM", "Error actualizando", e)
                resultMessage = "Error de red"
            }
            isLoading = false
            isEditing = false
        }
    }

    fun onDeleteClick(id: Int) {
        viewModelScope.launch {
            isLoading = true
            try {
                val resp = RetrofitClient.api.deleteCategory(id)
                if (resp.isSuccessful) {
                    resultMessage = "success"
                } else {
                    val errJson = resp.errorBody()?.string()
                    val err = Gson().fromJson(errJson, DeleteCategoryResponse::class.java)
                    resultMessage = err.message ?: "Error ${resp.code()}"
                }
            } catch (e: Exception) {
                Log.e("EditCategoryVM", "Error eliminando", e)
                resultMessage = "Error de red"
            }
            isLoading = false
        }
    }
}
