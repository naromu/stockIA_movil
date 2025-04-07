package com.example.stockia.viewmodel.login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockia.model.ConfirmEmailResponse
import com.example.stockia.model.ResetPasswordThreeRequest
import kotlinx.coroutines.launch

class ResetPasswordThreeViewModel: ViewModel()  {

    var password by mutableStateOf("")
        private set

    var confirmPassword by mutableStateOf("")
        private set

    var isPasswordVisible by mutableStateOf(false)
        private set

    var isConfirmPasswordVisible by mutableStateOf(false)
        private set

    var confirmPasswordError by mutableStateOf<String?>(null)
        private set


    var passwordErrors by mutableStateOf<List<String>>(emptyList())
        private set


    var isLoading by mutableStateOf(false)
        private set

    var resultMessage by mutableStateOf<String?>(null)
        private set

    var code by mutableStateOf("")



    val isFormValid: Boolean
        get() = password.isNotBlank()
                && confirmPassword.isNotBlank()
                && passwordErrors.isEmpty()
                && confirmPasswordError.isNullOrEmpty()


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

    fun clearResultMessage() {
        resultMessage = null
    }



    fun onRestablecerClick() {
        viewModelScope.launch {
            isLoading = true
            try {
                Log.d("ConfirmViewModel", "Boton oprimido" + code)

                val request = ResetPasswordThreeRequest(token = code,
                    newPassword =password,
                    confirmNewPassword = confirmPassword)

                val response = RetrofitClient.api.ResetPasswordThree(request)

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.status == "success") {
                        Log.d("ConfirmViewModel", "success")

                        resultMessage = "success"
                    } else {
                        Log.d("ConfirmViewModel", "inesperado")

                        resultMessage = body?.message ?: "Error inesperado"
                    }
                } else {
                    val errorJson = response.errorBody()?.string()
                    val gson = com.google.gson.Gson()
                    val errorResponse = gson.fromJson(errorJson, ConfirmEmailResponse::class.java)
                    resultMessage = errorResponse.message ?: "Error al confirmar"
                }
            } catch (e: Exception) {
                resultMessage = e.message ?: "Error desconocido"
            } finally {
                isLoading = false
            }
        }
    }

}