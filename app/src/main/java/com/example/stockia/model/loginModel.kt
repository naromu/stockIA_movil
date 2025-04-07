package com.example.stockia.model

data class RegisterRequest(
    val fullName: String,
    val companyName: String,
    val password: String,
    val confirmPassword: String,
    val email: String,
    val phone: String
)

data class RegisterResponse<T>(
    val status: String,
    val message: String?,
    val data: T? = null
)


data class ConfirmEmailRequest(
    val token: String
)

data class ConfirmEmailResponse(
    val status: String,
    val message: String?,
    val data: ConfirmEmailData? = null
)

data class ConfirmEmailData(
    val newToken: String?,
    val newExpiration: String?
)


data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val status: String,
    val message: String,
    val data: LoginData?
)

data class LoginData(
    val token: String
)
