package com.example.stockia.model

data class UserProfileResponse(
    val status: String,
    val message: String,
    val data: UserProfile
)

data class UserProfile(
    val id: Int,
    val full_name: String,
    val company_name: String,
    val email: String,
    val phone: String,
    val created_at: String,
    val updated_at: String
)
