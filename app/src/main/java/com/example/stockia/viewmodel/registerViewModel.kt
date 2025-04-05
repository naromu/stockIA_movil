package com.example.stockia.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.OffsetMapping
import androidx.lifecycle.ViewModel

class RegisterViewModel : ViewModel() {  // Cambiado a RegisterViewModel para coincidir con el contexto de registro

    // Campos del formulario
    var ownerName by mutableStateOf("")
        private set

    var companyName by mutableStateOf("")
        private set

    var email by mutableStateOf("")
        private set

    var phoneNumber by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var confirmPassword by mutableStateOf("")
        private set

    var isPasswordVisible by mutableStateOf(false)
        private set

    // Funciones para actualizar los estados
    fun onOwnerNameChange(newValue: String) {
        ownerName = newValue
    }

    fun onCompanyNameChange(newValue: String) {
        companyName = newValue
    }

    fun onEmailChange(newValue: String) {
        email = newValue
    }

    fun onPhoneNumberChange(newValue: String) {
        // Filtra solo dígitos y limita a 10 caracteres
        phoneNumber = newValue.take(10).filter { it.isDigit() }
    }

    fun onPasswordChange(newValue: String) {
        password = newValue
    }

    fun onConfirmPasswordChange(newValue: String) {
        confirmPassword = newValue
    }

    fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
    }

    fun onRegisterClick() {
        // Lógica de registro
        // Aquí puedes validar que password == confirmPassword
        // y hacer el llamado al backend
    }
}
