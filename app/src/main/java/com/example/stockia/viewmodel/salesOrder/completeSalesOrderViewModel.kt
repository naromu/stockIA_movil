package com.example.stockia.viewmodel.salesOrder

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockia.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class CompleteSalesOrderViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CompleteSalesOrderUiState())
    val uiState: StateFlow<CompleteSalesOrderUiState> = _uiState

    init {
        loadClients()
        loadStatusOptions()
        calculateTotal()
    }

    fun loadSelectedProducts(selectedProductIds: List<Int>) {
        viewModelScope.launch {
            val products = selectedProductIds.mapNotNull { productId ->
                // Llamamos al servicio para obtener cada producto por su ID
                val response = RetrofitClient.api.getProduct(productId)
                if (response.isSuccessful) {
                    // Convertimos el Product a SelectedProduct
                    val product = response.body()?.data
                    product?.let {
                        SelectedProduct(
                            id = it.id,
                            name = it.name,
                            description = it.description,
                            imageUrl = it.imageUrl,
                            unitPrice = it.unitPrice,
                            quantity = 1
                        )
                    }
                } else {
                    null
                }
            }

            // Actualizamos el estado con los productos obtenidos
            _uiState.update { it.copy(selectedProducts = products) }
            calculateTotal()
        }
    }

    fun onClientSelected(name: String) {
        val client = _uiState.value.clients.find { it.name == name }
        _uiState.update { it.copy(selectedClient = client) }
    }

    fun onStatusSelected(description: String) {
        val status = _uiState.value.statusOptions.find { it.description == description }
        _uiState.update { it.copy(selectedStatus = status) }
    }

    fun onDescriptionChange(newDescription: String) {
        _uiState.update { it.copy(description = newDescription) }
    }

    fun increaseQuantity(productId: Int) {
        _uiState.update { currentState ->
            val updatedProducts = currentState.selectedProducts.map {
                if (it.id == productId) it.copy(quantity = it.quantity + 1) else it
            }
            currentState.copy(selectedProducts = updatedProducts)
        }
        calculateTotal()
    }

    fun decreaseQuantity(productId: Int) {
        _uiState.update { currentState ->
            val updatedProducts = currentState.selectedProducts.map {
                if (it.id == productId && it.quantity > 1) it.copy(quantity = it.quantity - 1) else it
            }
            currentState.copy(selectedProducts = updatedProducts)
        }
        calculateTotal()
    }

    fun calculateTotal() {
        val total = _uiState.value.selectedProducts.sumOf { product ->
            (product.unitPrice.toDoubleOrNull() ?: 0.0) * product.quantity
        }
        _uiState.update { it.copy(totalAmount = total) }
    }

    fun submitSalesOrder(navController: androidx.navigation.NavController) {
        val client = _uiState.value.selectedClient ?: return
        val status = _uiState.value.selectedStatus ?: return
        val products = _uiState.value.selectedProducts

        val request = CreateSalesOrderRequest(
            customerId = client.id,
            statusId = status.id,
            salesOrderDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).format(Date()),
            notes = _uiState.value.description,
            items = products.map { SalesOrderItemRequest(it.id, it.quantity) }
        )

        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.createSalesOrder(request)
                if (response.isSuccessful) {
                    navController.navigate("SalesOrdersView") {
                        popUpTo("CompleteSalesOrderView") { inclusive = true }
                    }
                } else {
                    println("Error creando orden: ${response.code()}")
                }
            } catch (e: Exception) {
                println("Excepción al crear orden: ${e.message}")
            }
        }
    }

    private fun loadClients() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.getClients()
                if (response.isSuccessful) {
                    val clients = response.body()?.data ?: emptyList()
                    _uiState.update { it.copy(clients = clients) }
                } else {
                    println("Error al obtener clientes: ${response.code()}")
                }
            } catch (e: Exception) {
                println("Excepción cargando clientes: ${e.message}")
            }
        }
    }


    private fun loadStatusOptions() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.getStatus()
                if (response.isSuccessful) {
                    val categories = response.body()?.data?.statusCategories ?: emptyList()
                    val salesOrderCategory = categories.find { it.name == "sales_order" }
                    val statuses = salesOrderCategory?.statusTypes ?: emptyList()
                    _uiState.update { it.copy(statusOptions = statuses) }
                }
            } catch (e: Exception) {
                println("Error cargando estados: ${e.message}")
            }
        }
    }
}

// UI STATE

data class CompleteSalesOrderUiState(
    val clients: List<Client> = emptyList(),
    val selectedClient: Client? = null,
    val statusOptions: List<StatusType> = emptyList(),
    val selectedStatus: StatusType? = null,
    val description: String = "",
    val selectedProducts: List<SelectedProduct> = emptyList(),
    val totalAmount: Double = 0.0
)


data class SelectedProduct(
    val id: Int,
    val name: String,
    val description: String,
    val imageUrl: String?,
    val unitPrice: String,
    val quantity: Int
)
