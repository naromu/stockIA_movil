package com.example.stockia.model

// ---------- Peticiones ----------
data class PurchaseReturnItemRequest(
    val productId: Int,
    val quantity: Int
)

data class CreatePurchaseReturnRequest(
    val purchaseOrderId: Int,
    val returnDate: String,
    val notes: String,
    val items: List<PurchaseReturnItemRequest>
)

data class UpdatePurchaseReturnRequest(
    val purchaseOrderId: Int,
    val returnDate: String,
    val notes: String,
    val items: List<PurchaseReturnItemRequest>
)

// ---------- Respuestas ----------
data class PurchaseReturn(
    val id: Int,
    val user_id: Int,
    val purchase_order_id: Int,
    val return_date: String,
    val notes: String,
    val created_at: String,
    val updated_at: String
)

data class PurchaseReturnDetail(
    val id: Int,
    val purchase_order_id: Int,
    val return_date: String,
    val notes: String,
    val items: List<PurchaseReturnItemResponse>
)

data class PurchaseReturnItemResponse(
    val product_id: Int,
    val quantity: Int,
    val product_name: String,
    val product_description: String
)

// ---------- Envolturas ----------
data class CreatePurchaseReturnResponse(
    val status: String,
    val message: String?,
    val data: PurchaseReturn
)

data class PurchaseReturnsListResponse(
    val status: String,
    val message: String?,
    val data: List<PurchaseReturn>
)

data class PurchaseReturnDetailResponse(
    val status: String,
    val message: String?,
    val data: PurchaseReturnDetail
)

data class DeletePurchaseReturnResponse(
    val status: String,
    val message: String?
)
