package com.example.stockia.model

import com.google.gson.annotations.SerializedName


// Confirmed-sales
data class ConfirmedSalesResponse(
    val status: String,
    val message: String,
    val data: List<ConfirmedSale>
)
data class ConfirmedSale(
    @SerializedName("product_id") val productId: Int,
    val sales: List<Sale>,
    val stock: Int
)
data class Sale(
    val date: String,
    val quantity: Int
)

// Request de predicción múltiple
data class PredictRequest(
    val status: String,
    val message: String,
    val data: List<ConfirmedSale>
)

data class NavSelectedProduct(
    val id: Int,
    val initialQuantity: Int
)

// Respuesta de predicciones
data class PredictionsResponse(
    val status: String,
    val message: String,
    val data: List<Prediction>
)
data class Prediction(
    @SerializedName("product_id") val productId: Int,
    @SerializedName("recommended_units") val recommendedUnits: Int?,
    @SerializedName("shortage_date") val shortageDate: String?,
    val message: String,
    val forecast: List<Map<String, Any>>,
    @SerializedName("replenishment_plan") val replenishmentPlan: String?
)