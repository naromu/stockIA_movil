// stockModel.kt
package com.example.stockia.model

data class UpdateStockRequest(
    val quantity: Int
)

data class UpdateStockResponse(
    val status: String,
    val message: String
)