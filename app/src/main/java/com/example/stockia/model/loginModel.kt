package com.example.stockia.model

import com.google.gson.annotations.SerializedName

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

data class ResetPasswordOneRequest(
    val email: String
)

data class ResetPasswordOneResponse(
    val status: String,
    val message: String,
    val data: LoginData?
)

data class ResetPasswordThreeRequest(
    val token: String,
    val newPassword: String,
    val confirmNewPassword: String,
)

data class ResetPasswordThreeResponse(
    val status: String,
    val message: String,
    val data: LoginData?
)

data class ProfileData(
    val id: Int,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("company_name") val companyName: String,
    val email: String,
    val phone: String
)

data class ProfileResponse(
    val status: String,
    val message: String,
    val data: ProfileData?
)

