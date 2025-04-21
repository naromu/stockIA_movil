package com.example.stockia.model

// Modelo base que representa un proveedor
data class Provider(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String,
    val address: String,
    val user_id: Int,
    val created_at: String,
    val updated_at: String
)

// Request para crear proveedor
data class CreateProviderRequest(
    val name: String,
    val email: String,
    val phone: String,
    val address: String
)

// Response al crear proveedor
data class CreateProviderResponse(
    val status: String,
    val message: String?,
    val data: Provider
)

// Response al obtener un proveedor
data class GetProviderResponse(
    val status: String,
    val message: String?,
    val data: Provider
)

// Response al listar proveedores
data class ProvidersResponse(
    val status: String,
    val message: String?,
    val data: List<Provider>
)

// Request para actualizar proveedor
data class UpdateProviderRequest(
    val name: String,
    val email: String,
    val phone: String,
    val address: String
)

// Response al actualizar proveedor
data class UpdateProviderResponse(
    val status: String,
    val message: String?,
    val data: Provider
)

// Response al eliminar proveedor
data class DeleteProviderResponse(
    val status: String,
    val message: String?
)
