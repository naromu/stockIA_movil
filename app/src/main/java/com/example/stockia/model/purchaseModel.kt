// Modelo para item de orden de compra
data class PurchaseOrderItem(
    val product_id: Int,
    val quantity: Int,
    val unit_cost: Double
)

// Detalle de una orden de compra
data class PurchaseOrder(
    val id: Int,
    val user_id: Int,
    val supplier_id: Int,
    val status_id: Int,
    val total_amount: String,
    val notes: String?,
    val purchase_order_date: String,
    val supplier_name: String?,
    val status_name: String?,
    val products: List<PurchaseOrderProduct>
)


data class PurchaseOrderProduct(
    val id: Int,
    val purchase_order_id: Int,
    val product_id: Int,
    val quantity: Int,
    val unit_cost: String, // ← ojo: viene como string en JSON
    val product_name: String?,
    val product_description: String?
)

// Request para crear o actualizar
data class PurchaseOrderItemRequest(
    val productId: Int,
    val quantity: Int,
    val unitCost: Double
)

data class CreatePurchaseOrderRequest(
    val supplierId: Int,
    val statusId: Int,
    val notes: String,
    val purchaseOrderDate: String,
    val items: List<PurchaseOrderItemRequest>
)

// Wrapper de respuesta de lista
data class PurchaseOrdersListResponse(
    val status: String,
    val message: String,
    val data: List<PurchaseOrder>
)

// Wrapper de respuesta individual
data class PurchaseOrderResponse(
    val status: String,
    val message: String,
    val data: PurchaseOrder
)
