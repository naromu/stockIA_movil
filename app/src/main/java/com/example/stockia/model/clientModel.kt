package com.example.stockia.model

// Cliente que recibes del backend
data class Client(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String,
    val address: String,
    val user_id: Int,
    val created_at: String,
    val updated_at: String
)

// Lista de clientes
data class ClientsResponse(
    val status: String,
    val message: String?,
    val data: List<Client>
)

// Crear cliente - solicitud
data class CreateClientRequest(
    val name: String,
    val email: String,
    val phone: String,
    val address: String
)

// Crear cliente - respuesta
data class CreateClientResponse(
    val status: String,
    val message: String?,
    val data: Client
)

// Obtener cliente por ID
data class GetClientResponse(
    val status: String,
    val message: String?,
    val data: Client
)

// Actualizar cliente
data class UpdateClientRequest(
    val name: String,
    val email: String,
    val phone: String,
    val address: String
)

data class UpdateClientResponse(
    val status: String,
    val message: String?,
    val data: Client
)

// Eliminar cliente
data class DeleteClientResponse(
    val status: String,
    val message: String?
)
