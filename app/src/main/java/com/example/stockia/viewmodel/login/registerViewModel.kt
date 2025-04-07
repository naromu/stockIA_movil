package com.example.stockia.viewmodel.login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockia.model.RegisterRequest
import com.example.stockia.model.RegisterResponse
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

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

    var isConfirmPasswordVisible by mutableStateOf(false)
        private set

    var emailError by mutableStateOf<String?>(null)
        private set

    var confirmPasswordError by mutableStateOf<String?>(null)
        private set

    var registrationResult by mutableStateOf<String?>(null)
        private set

    var passwordErrors by mutableStateOf<List<String>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set



    val isFormValid: Boolean
        get() = ownerName.isNotBlank()
                && companyName.isNotBlank()
                && phoneNumber.length == 10
                && password.isNotBlank()
                && confirmPassword.isNotBlank()
                && emailError == null
                && confirmPasswordError == null

    // Funciones para actualizar los estados
    fun onOwnerNameChange(newValue: String) {
        ownerName = newValue
    }

    fun onCompanyNameChange(newValue: String) {
        companyName = newValue
    }


    fun onEmailChange(newValue: String) {
        email = newValue
        emailError = if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            "Correo no válido"
        } else null
    }

    fun onPhoneNumberChange(newValue: String) {
        // Filtra solo dígitos y limita a 10 caracteres
        phoneNumber = newValue.take(10).filter { it.isDigit() }
    }

    fun onPasswordChange(newValue: String) {
        password = newValue
        passwordErrors = getPasswordValidationErrors(password)

        confirmPasswordError = if (confirmPassword != password) {
            "Las contraseñas no coinciden"
        } else null
    }


    private fun getPasswordValidationErrors(password: String): List<String> {
        val errors = mutableListOf<String>()

        if (password.length < 8) {
            errors.add("Debe tener al menos 8 caracteres")
        }
        if (!password.any { it.isUpperCase() }) {
            errors.add("Debe contener al menos una letra mayúscula")
        }
        if (!password.any { it.isLowerCase() }) {
            errors.add("Debe contener al menos una letra minúscula")
        }
        if (!password.any { it.isDigit() }) {
            errors.add("Debe contener al menos un número")
        }

        return errors
    }


    fun onConfirmPasswordChange(newValue: String) {
        confirmPassword = newValue
        confirmPasswordError = if (confirmPassword != password) {
            "Las contraseñas no coinciden"
        } else null
    }

    fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
    }

    fun toggleConfirmPasswordVisibility() {
        isConfirmPasswordVisible = !isConfirmPasswordVisible
    }



    fun onRegisterClick() {
        viewModelScope.launch {
            isLoading = true

            try {
                val request = RegisterRequest(
                    fullName = ownerName,
                    companyName = companyName,
                    email = email,
                    phone = phoneNumber,
                    password = password,
                    confirmPassword = confirmPassword
                )

                val response = RetrofitClient.api.registerUser(request)

                val body = try {
                    response.body() ?: run {
                        // Parsear el error como RegisterResponse si no hay body
                        val errorJson = response.errorBody()?.string()
                        com.google.gson.Gson().fromJson(errorJson, RegisterResponse::class.java)
                    }
                } catch (e: Exception) {
                    null
                }

                if (body == null) {
                    registrationResult = "No se pudo procesar la respuesta"
                    return@launch
                }

                Log.d("RegisterViewModel", "status: ${body.status} | message: ${body.message}")

                if (body.status == "success") {
                    registrationResult = "success"
                } else {
                    registrationResult = body.message ?: "Ocurrió un error"
                }

            } catch (e: Exception) {
                Log.d("RegisterViewModel", "Exception: ${e.message}")
                registrationResult = e.message ?: "Error desconocido"
            }
        }
    }



    fun clearRegistrationResult() {
        registrationResult = null
    }
}
