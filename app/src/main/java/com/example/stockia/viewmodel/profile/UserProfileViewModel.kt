package com.example.stockia.viewmodel.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class UserProfileViewModel : ViewModel() {

    var fullName by mutableStateOf("")
    var companyName by mutableStateOf("")
    var phone by mutableStateOf("")
    var email by mutableStateOf("")

    private var originalFullName = ""
    private var originalCompanyName = ""
    private var originalPhone = ""

    var isLoading by mutableStateOf(false)
    var resultMessage by mutableStateOf<String?>(null)
    val isModified: Boolean
        get() = fullName != originalFullName || companyName != originalCompanyName || phone != originalPhone

    fun loadProfile() {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = RetrofitClient.api.getUserProfile()
                if (response.isSuccessful) {
                    response.body()?.data?.let { user ->
                        fullName = user.full_name
                        companyName = user.company_name
                        phone = user.phone
                        email = user.email

                        originalFullName = user.full_name
                        originalCompanyName = user.company_name
                        originalPhone = user.phone
                    }
                } else {
                    resultMessage = "Error al obtener perfil: ${response.code()}"
                }
            } catch (e: Exception) {
                resultMessage = "Error de red"
            }
            isLoading = false
        }
    }


    fun onFullNameChange(newValue: String) {
        fullName = newValue
    }

    fun onCompanyNameChange(newValue: String) {
        companyName = newValue
    }

    fun onPhoneChange(newValue: String) {
        phone = newValue
    }

    fun clearResultMessage() {
        resultMessage = null
    }
}
