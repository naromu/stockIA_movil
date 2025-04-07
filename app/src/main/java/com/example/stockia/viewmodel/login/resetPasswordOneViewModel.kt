package com.example.stockia.viewmodel.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ResetPasswordOneViewModel: ViewModel()  {

    var email by mutableStateOf("")
        private set

    var resultMessage by mutableStateOf<String?>(null)
        private set

    val isFormValid: Boolean
        get() = email.isNotBlank()

    var isLoading by mutableStateOf(false)
        private set

    fun clearResultMessage() {
        resultMessage = null
    }

    var emailError by mutableStateOf<String?>(null)
        private set

    fun onEmailChange(newValue: String) {
        email = newValue
        emailError = if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            "Correo no válido"
        } else null
    }

    fun onSendClick() {
        resultMessage = "success"


    }

}