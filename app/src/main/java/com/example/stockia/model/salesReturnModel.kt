package com.example.stockia.model

    // Modelo de un item en la devolución
    data class SalesReturnItem(
        val id: Int,
        val sales_return_id: Int,
        val product_id: Int,
        val quantity: Int,
        val status_id: Int,
        val created_at: String,
        val updated_at: String,
        val product_name: String,
        val product_description: String,
        val status_name: String
    )

// Devolución simple (para el listado)
data class SalesReturn(
    val id: Int,
    val user_id: Int,
    val sales_order_id: Int,
    val return_date: String,
    val notes: String,
    val created_at: String,
    val updated_at: String
)

// Devolución detallada
data class SalesReturnDetail(
    val id: Int,
    val user_id: Int,
    val sales_order_id: Int,
    val return_date: String,
    val notes: String,
    val created_at: String,
    val updated_at: String,
    val items: List<SalesReturnItem>
)

// Response para listar devoluciones
data class SalesReturnsResponse(
    val status: String,
    val message: String?,
    val data: List<SalesReturn>
)

// Response para obtener devolución por ID
data class GetSalesReturnResponse(
    val status: String,
    val message: String?,
    val data: SalesReturnDetail
)

data class CreateSalesReturnRequest(
    val salesOrderId: Int,
    val returnDate: String,
    val notes: String,
    val items: List<SalesReturnItemRequest>
)

data class SalesReturnItemRequest(
    val productId: Int,
    val quantity: Int,
    val statusId: Int
)

// Crear devolución - response
data class CreateSalesReturnResponse(
    val status: String,
    val message: String?,
    val data: SalesReturn
)

// Actualizar devolución
data class UpdateSalesReturnRequest(
    val salesOrderId: Int,
    val returnDate: String,
    val notes: String,
    val items: List<SalesReturnItemRequest>
)

data class UpdateSalesReturnResponse(
    val status: String,
    val message: String?,
    val data: SalesReturn
)

// Eliminar devolución
data class DeleteSalesReturnResponse(
    val status: String,
    val message: String?
)

data class ProductReturnItem(
    val id: Int,
    val product_id: Int,
    val quantity: Int,
    val status_id: Int,
    val product_name: String,
    val product_description: String,
    val status_name: String
)

