package com.example.stockia.model

import CreatePurchaseOrderRequest
import MeasurementsResponse
import PurchaseOrderResponse
import PurchaseOrdersListResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path
import retrofit2.http.PATCH


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

    // Clientes
    @GET("customers")
    suspend fun getClients(): Response<ClientsResponse>

    @GET("customers/{id}")
    suspend fun getClientById(@Path("id") id: Int): Response<GetClientResponse>

    @POST("customers")
    suspend fun createClient(@Body request: CreateClientRequest): Response<CreateClientResponse>

    @PUT("customers/{id}")
    suspend fun updateClient(@Path("id") id: Int, @Body request: UpdateClientRequest): Response<UpdateClientResponse>

    @DELETE("customers/{id}")
    suspend fun deleteClient(@Path("id") id: Int): Response<DeleteClientResponse>

    // Providers
    @GET("suppliers")
    suspend fun getProviders(): Response<ProvidersResponse>

    @POST("suppliers")
    suspend fun createProvider(@Body request: CreateProviderRequest): Response<CreateProviderResponse>

    @GET("suppliers/{id}")
    suspend fun getProviderById(@Path("id") id: Int): Response<GetProviderResponse>

    @PUT("suppliers/{id}")
    suspend fun updateProvider(@Path("id") id: Int, @Body request: UpdateProviderRequest): Response<UpdateProviderResponse>

    @DELETE("suppliers/{id}")
    suspend fun deleteProvider(@Path("id") id: Int): Response<DeleteProviderResponse>

    // Sales Orders
    @POST("sales-orders")
    suspend fun createSalesOrder(@Body request: CreateSalesOrderRequest): Response<SalesOrderResponse>

    @GET("sales-orders")
    suspend fun getSalesOrders(): Response<SalesOrdersListResponse>

    @GET("sales-orders/{id}")
    suspend fun getSalesOrderById(@Path("id") id: Int): Response<SalesOrderDetailResponse>

    @PUT("sales-orders/{id}")
    suspend fun updateSalesOrder(
        @Path("id") id: Int,
        @Body request: CreateSalesOrderRequest
    ): Response<SalesOrderResponse>

    @DELETE("sales-orders/{id}")
    suspend fun deleteSalesOrder(@Path("id") id: Int): Response<DeleteSalesOrderResponse>

    //Purchase Orders

    @GET("/purchase-orders")
    suspend fun getPurchaseOrders(): Response<PurchaseOrdersListResponse>

    @GET("/purchase-orders/{id}")
    suspend fun getPurchaseOrderById(
        @Path("id") id: Int
    ): Response<PurchaseOrderResponse>

    @PUT("/purchase-orders/{id}")
    suspend fun updatePurchaseOrder(
        @Path("id") id: Int,
        @Body request: CreatePurchaseOrderRequest
    ): Response<GenericResponse>

    @POST("/purchase-orders")
    suspend fun createPurchaseOrder(
        @Body request: CreatePurchaseOrderRequest
    ): Response<GenericResponse>


    //Products
    @GET("products")
    suspend fun getProducts(): Response<ProductsResponse>

    @Multipart
    @POST("products")
    suspend fun createProduct(
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("unitPrice") unitPrice: RequestBody,
        @Part("unitCost") unitCost: RequestBody,
        @Part("quantity") quantity: RequestBody,
        @Part("barcode") barcode: RequestBody,
        @Part("categoryId") categoryId: RequestBody,
        @Part("unitOfMeasureId") unitOfMeasureId: RequestBody,
        @Part image: MultipartBody.Part?
    ): Response<CreateProductResponse>

    @GET("products/{id}")
    suspend fun getProduct(@Path("id") id: Int): Response<GetProductResponse>

    @DELETE("products/{id}")
    suspend fun deleteProduct(@Path("id") id: Int): Response<GenericResponse>

    @Multipart
    @PUT("products/{id}")
    suspend fun updateProductWithImage(
        @Path("id") id: Int,
        @PartMap fields: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part image: MultipartBody.Part?
    ): Response<UpdateProductResponse>

    //Stock update
    @PATCH("products/{id}/stock")
    suspend fun updateStock(
        @Path("id") productId: Int,
        @Body stockUpdateRequest: UpdateStockRequest
    ): Response<UpdateStockResponse>


    //measurement
    @GET("measurements")
    suspend fun getMeasurements(): Response<MeasurementsResponse>

    //Status
    @GET("status")
    suspend fun getStatus(): Response<StatusResponse>



}
