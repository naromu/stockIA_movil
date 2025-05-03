package com.example.stockia.model

import com.google.gson.annotations.SerializedName

data class StatusType(
    val id: Int,
    val name: String,
    val description: String
)

data class StatusCategory(
    val id: Int,
    val name: String,
    val description: String,
    @SerializedName("status_types")
    val statusTypes: List<StatusType>
)

data class StatusData(
    @SerializedName("status_categories")
    val statusCategories: List<StatusCategory>
)

data class StatusResponse(
    val status: String,
    val message: String,
    val data: StatusData
)
