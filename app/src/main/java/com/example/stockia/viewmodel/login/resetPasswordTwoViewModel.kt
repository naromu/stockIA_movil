package com.example.stockia.viewmodel.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.stockia.routes.Routes

class ResetPasswordTwoViewModel: ViewModel()  {

    var code by mutableStateOf("")
        private set


    val isFormValid: Boolean
        get() = code.isNotBlank()


    fun onCodeChange(newValue: String) {
        code = newValue
    }

    fun onContinuosClick(navController: NavController) {

        navController.navigate("${Routes.ResetPasswordThreeView}?code=${code}")
    }

}