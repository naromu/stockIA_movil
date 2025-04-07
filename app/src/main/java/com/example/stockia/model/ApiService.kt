package com.example.stockia.model

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("users/register")
    suspend fun registerUser(@Body request: RegisterRequest): Response<RegisterResponse<Any>>

    @POST("/users/confirm-email")
    suspend fun confirmEmail(@Body request: ConfirmEmailRequest): Response<ConfirmEmailResponse>

    @POST("/users/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("/users/forgot-password")
    suspend fun ResetPasswordOne(@Body request: ResetPasswordOneRequest): Response<ResetPasswordOneResponse>

    @POST("/users/reset-password")
    suspend fun ResetPasswordThree(@Body request: ResetPasswordThreeRequest): Response<ResetPasswordThreeResponse>

    @POST("/users/logout")
    suspend fun logout(): Response<ConfirmEmailResponse>

}
