package com.example.stockia.viewmodel.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ResetPasswordTwoViewModel: ViewModel()  {

    var code by mutableStateOf("")
        private set


    val isFormValid: Boolean
        get() = code.isNotBlank()


    fun onCodeChange(newValue: String) {
        code = newValue
    }

    fun onContinuosClick() {

        isFormValid

    }

}