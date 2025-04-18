package com.example.stockia.model

data class Category(
    val id: Int,
    val name: String,
    val user_id: Int,
    val created_at: String,
    val updated_at: String
)

data class CategoriesResponse(
    val status: String,
    val message: String,
    val data: List<Category>
)


data class CreateCategoryRequest(
    val name: String
)

data class CreateCategoryResponse(
    val status: String,
    val message: String?,
    val data: Category
)

data class GetCategoryResponse(
    val status: String,
    val message: String?,
    val data: Category
)

data class UpdateCategoryRequest(val name: String)
data class UpdateCategoryResponse(
    val status: String,
    val message: String?,
    val data: Category
)

data class DeleteCategoryResponse(
    val status: String,
    val message: String?
)