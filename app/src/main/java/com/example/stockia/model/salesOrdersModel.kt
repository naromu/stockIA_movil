package com.example.stockia.model

// ---------- Peticiones ----------
data class SalesOrderItemRequest(
    val productId: Int,
    val quantity: Int
)

data class CreateSalesOrderRequest(
    val customerId: Int,
    val statusId: Int,
    val salesOrderDate: String,
    val notes: String,
    val items: List<SalesOrderItemRequest>
)

// ---------- Respuestas ----------
data class SalesOrder(
    val id: Int,
    val user_id: Int,
    val customer_id: Int,
    val status_id: Int,
    val total_amount: String,
    val notes: String,
    val sales_order_date: String,
    val created_at: String,
    val updated_at: String,
    val customer_name: String?,
    val status_name: String?
)

data class SalesOrderDetail(
    val id: Int,
    val user_id: Int,
    val customer_id: Int,
    val status_id: Int,
    val total_amount: String,
    val notes: String,
    val sales_order_date: String,
    val created_at: String,
    val updated_at: String,
    val customer_name: String,
    val status_name: String,
    val products: List<SalesOrderProduct>
)

data class SalesOrderProduct(
    val id: Int,
    val sales_order_id: Int,
    val product_id: Int,
    val quantity: Int,
    val unit_price: String,
    val created_at: String,
    val updated_at: String,
    val product_name: String,
    val product_description: String
)

// ---------- Envolturas ----------
data class SalesOrderResponse(
    val status: String,
    val message: String?,
    val data: SalesOrder
)

data class SalesOrdersListResponse(
    val status: String,
    val message: String?,
    val data: List<SalesOrder>
)

data class SalesOrderDetailResponse(
    val status: String,
    val message: String?,
    val data: SalesOrderDetail
)

data class DeleteSalesOrderResponse(
    val status: String,
    val message: String?
)

data class BaseResponse(
    val status: String,
    val message: String
)

