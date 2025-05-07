package com.example.stockia.viewmodel.purchaseOrder

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockia.model.Category
import com.example.stockia.model.Product
import com.example.stockia.model.Provider
import kotlinx.coroutines.launch

class NewPurchasesOrderViewModel : ViewModel() {

    var products by mutableStateOf<List<Product>>(emptyList())
        private set



    var searchText by mutableStateOf("")
        private set


    var selectedProducts by mutableStateOf<Set<Int>>(emptySet())
        private set

    var resultMessage by mutableStateOf<String?>(null)
        private set

    var showFilterDialog by mutableStateOf(false)
        private set

    val filteredProducts: List<Product>
        get() = products.filter {
            it.name.contains(searchText, ignoreCase = true)
        }

    var categories by mutableStateOf<List<Category>>(emptyList())
        private set

    fun getCategoryName(categoryId: Int): String {
        return categories.find { it.id == categoryId }?.name ?: "Sin categoría"
    }

    fun loadCategories() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.getCategories()
                if (response.isSuccessful) {
                    categories = response.body()?.data ?: emptyList()
                } else {
                    resultMessage = "Error ${response.code()} al obtener categorías"
                }
            } catch (e: Exception) {
                resultMessage = e.localizedMessage ?: "Error desconocido al cargar categorías"
            }
        }
    }



    fun loadProducts() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.getProducts()
                if (response.isSuccessful) {
                    products = response.body()?.data ?: emptyList()
                } else {
                    resultMessage = "Error ${response.code()} al obtener productos"
                }
            } catch (e: Exception) {
                resultMessage = e.localizedMessage ?: "Error desconocido al cargar productos"
            }
        }
    }


    fun onSearchTextChange(newValue: String) {
        searchText = newValue
    }

    fun toggleFilterDialog() {
        showFilterDialog = !showFilterDialog
    }

    fun toggleProductSelection(productId: Int) {
        selectedProducts = if (selectedProducts.contains(productId)) {
            selectedProducts - productId
        } else {
            selectedProducts + productId
        }
    }

    fun isProductSelected(productId: Int): Boolean {
        return selectedProducts.contains(productId)
    }

    fun clearResultMessage() {
        resultMessage = null
    }
}
