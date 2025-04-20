package com.example.stockia.model

import com.google.gson.annotations.SerializedName

data class Product(
    val id: Int,
    @SerializedName("user_id") val userId: Int,
    val name: String,
    val description: String,
    @SerializedName("unit_price") val unitPrice: String,
    @SerializedName("unit_cost") val unitCost: String,
    val quantity: String,
    @SerializedName("image_url") val imageUrl: String,
    val barcode: String,
    @SerializedName("category_id") val categoryId: Int,
    @SerializedName("unit_of_measure_id") val unitOfMeasureId: Int,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String
)

data class ProductsResponse(
    val status: String,
    val message: String,
    val data: List<Product>
)

data class CreateProductResponse(
    val status: String,
    val message: String,
    val data: Product
)

data class GetProductResponse(
    val status: String,
    val message: String,
    val data: Product
)

data class UpdateProductResponse(
    val status: String,
    val message: String,
    val data: Product
)

data class GenericResponse(
    val status: String,
    val message: String
)

