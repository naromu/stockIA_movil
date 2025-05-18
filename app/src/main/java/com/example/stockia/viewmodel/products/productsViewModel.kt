package com.example.stockia.viewmodel.products

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockia.model.LoginResponse
import com.example.stockia.model.Product
import com.example.stockia.model.ProductsResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class ProductsViewModel : ViewModel() {

    var searchQuery by mutableStateOf("")
        private set

    var products by mutableStateOf<List<Product>>(emptyList())
        private set

    var filteredProducts by mutableStateOf<List<Product>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var resultMessage by mutableStateOf<String?>(null)
        private set

    fun onSearchQueryChange(newValue: String) {
        searchQuery = newValue
        filterProducts()
    }

    fun onBarCodeScanned(barcode: String) {
        searchQuery = barcode
        filterProducts()
    }

    init {
        loadProducts()
    }

    fun clearResultMessage() {
        resultMessage = null
    }

    private fun filterProducts() {
        val query = searchQuery.trim().lowercase()
        filteredProducts = if (query.isEmpty()) {
            products
        } else {
            products.filter { product ->
                product.name.lowercase().contains(query) ||
                        product.barcode?.lowercase()?.contains(query) == true
            }
        }
    }

    fun loadProducts() {
        viewModelScope.launch {
            isLoading = true
            try {
                val response: Response<ProductsResponse> = RetrofitClient.api.getProducts()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.status == "success") {
                        products = body.data
                        filterProducts()
                        resultMessage = null
                        Log.d("ProductsVM", "Productos cargados: ${products.size}")
                    } else {
                        resultMessage = body?.message ?: "Respuesta inesperada"
                    }
                } else {
                    val errorJson = response.errorBody()?.string()
                    val error = Gson().fromJson(errorJson, LoginResponse::class.java)
                    resultMessage = error?.message ?: "Error desconocido"
                    Log.e("NewCategoryVM", "HTTP ${response.code()} - ${response.errorBody()?.string()}")

                }
            }  catch (e: IOException) {
                resultMessage = "Error de red"
            }
            catch (e: Exception) {
                resultMessage = e.localizedMessage ?: "Error desconocido"
            }
            isLoading = false
        }
    }
}
