package com.example.stockia.viewmodel.purchaseOrder

import CreatePurchaseOrderRequest
import PurchaseOrderItemRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.stockia.model.*
import com.example.stockia.viewmodel.salesOrder.SelectedProduct
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class CompletePurchaseOrderUiState(
    val suppliers: List<Provider> = emptyList(),
    val selectedSupplier: Provider? = null,
    val statusOptions: List<StatusType> = emptyList(),
    val selectedStatus: StatusType? = null,
    val description: String = "",
    val selectedProducts: List<SelectedProduct> = emptyList(),
    val totalAmount: Double = 0.0,
    val isLoading: Boolean = false

)


class CompletePurchaseOrderViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CompletePurchaseOrderUiState())
    val uiState: StateFlow<CompletePurchaseOrderUiState> = _uiState

    init {
        loadSuppliers()
        loadStatusOptions()
    }

    fun onSupplierSelected(name: String) {
        val supplier = _uiState.value.suppliers.find { it.name == name }
        _uiState.update { it.copy(selectedSupplier = supplier) }
    }

    fun onStatusSelected(description: String) {
        val status = _uiState.value.statusOptions.find { it.description == description }
        _uiState.update { it.copy(selectedStatus = status) }
    }

    fun onDescriptionChange(newDescription: String) {
        _uiState.update { it.copy(description = newDescription) }
    }

    fun increaseQuantity(productId: Int) {
        _uiState.update {
            it.copy(selectedProducts = it.selectedProducts.map { p ->
                if (p.id == productId) p.copy(quantity = p.quantity + 1) else p
            })
        }
        calculateTotal()
    }

    fun decreaseQuantity(productId: Int) {
        _uiState.update {
            it.copy(selectedProducts = it.selectedProducts.map { p ->
                if (p.id == productId && p.quantity > 1) p.copy(quantity = p.quantity - 1) else p
            })
        }
        calculateTotal()
    }

    fun calculateTotal() {
        val total = _uiState.value.selectedProducts.sumOf { product ->
            (product.unitPrice.toDoubleOrNull() ?: 0.0) * product.quantity
        }
        _uiState.update { it.copy(totalAmount = total) }
    }


    fun submitPurchaseOrder(navController: NavController) {
        val supplier = _uiState.value.selectedSupplier ?: return
        val status = _uiState.value.selectedStatus ?: return
        val products = _uiState.value.selectedProducts

        val request = CreatePurchaseOrderRequest(
            supplierId = supplier.id,
            statusId = status.id,
            purchaseOrderDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).format(Date()),
            notes = _uiState.value.description,
            items = products.map { PurchaseOrderItemRequest(it.id, it.quantity, it.unitPrice.toDoubleOrNull() ?: 0.0) }
        )

        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.createPurchaseOrder(request)
                if (response.isSuccessful) {
                    navController.navigate("PurchasesOrdersView") {
                        popUpTo("CompletePurchaseOrderView") { inclusive = true }
                    }
                }
            } catch (e: Exception) {
                println("Error al crear orden de compra: ${e.message}")
            }
        }
    }

    fun loadSelectedProducts(pre: List<NavSelectedProduct>) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val products = pre.mapNotNull { navItem ->
                val response = RetrofitClient.api.getProduct(navItem.id)
                response.body()?.data?.let { prod ->
                    SelectedProduct(
                        id = prod.id,
                        name = prod.name,
                        description = prod.description,
                        imageUrl = prod.imageUrl,
                        unitPrice = prod.unitCost,
                        quantity = navItem.initialQuantity  // ¡usa la qty del NavSelectedProduct!
                    )
                }
            }
            _uiState.update {
                it.copy(selectedProducts = products, isLoading = false)
            }
            calculateTotal()
        }
    }

    private fun loadSuppliers() {
        viewModelScope.launch {
            val response = RetrofitClient.api.getProviders()
            if (response.isSuccessful) {
                val suppliers = response.body()?.data ?: emptyList()
                _uiState.update { it.copy(suppliers = suppliers) }
            }
        }
    }

    private fun loadStatusOptions() {
        viewModelScope.launch {
            val response = RetrofitClient.api.getStatus()
            if (response.isSuccessful) {
                val categories = response.body()?.data?.statusCategories ?: emptyList()
                val purchaseOrderCategory = categories.find { it.name == "purchase_order" }
                val statuses = purchaseOrderCategory?.statusTypes ?: emptyList()
                _uiState.update { it.copy(statusOptions = statuses) }
            }
        }
    }
}
