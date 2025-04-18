package com.example.stockia.model

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

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

    @GET("users/profile")
    suspend fun getProfile(): Response<ProfileResponse>

    //Categories
    @GET("categories")
    suspend fun getCategories(): Response<CategoriesResponse>

    @POST("categories")
    suspend fun createCategory(@Body request: CreateCategoryRequest): Response<CreateCategoryResponse>

    @GET("categories/{id}")
    suspend fun getCategoryById(@Path("id") id: Int): Response<GetCategoryResponse>

    @PUT("categories/{id}")
    suspend fun updateCategory(@Path("id") id: Int, @Body request: UpdateCategoryRequest): Response<UpdateCategoryResponse>

    @DELETE("categories/{id}")
    suspend fun deleteCategory(@Path("id") id: Int): Response<DeleteCategoryResponse>


}
