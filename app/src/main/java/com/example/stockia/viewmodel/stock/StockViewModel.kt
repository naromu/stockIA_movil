package com.example.stockia.viewmodel.stock

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockia.model.Product
import com.example.stockia.model.ProductsResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class StockViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(StockUiState())
    val uiState: StateFlow<StockUiState> = _uiState

    var searchQuery by mutableStateOf("")
        private set

    var filteredProducts by mutableStateOf<List<Product>>(emptyList())
        private set

    fun onSearchQueryChange(newValue: String) {
        searchQuery = newValue
        filterProducts()
    }

    private fun filterProducts() {
        filteredProducts = if (searchQuery.isBlank()) {
            _uiState.value.products
        } else {
            _uiState.value.products.filter {
                it.name.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    fun loadProducts() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.getProducts()
                if (response.isSuccessful) {
                    val data = response.body()?.data ?: emptyList()
                    _uiState.update { it.copy(products = data) }
                    filterProducts()
                } else {
                    println("Error al cargar productos: ${response.code()}")
                }
            }catch (e: IOException) {
                println("Error de red")
            }
            catch (e: Exception) {
                println("Excepción al cargar productos: ${e.message}")
            }
        }
    }
}

data class StockUiState(
    val products: List<Product> = emptyList()
)
