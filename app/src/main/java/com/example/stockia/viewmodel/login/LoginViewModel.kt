package com.example.stockia.viewmodel.login

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockia.model.LoginRequest
import com.example.stockia.model.LoginResponse
import com.example.stockia.utils.SharedPreferencesHelper
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.io.IOException

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var isPasswordVisible by mutableStateOf(false)
        private set

    var loginResult by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    private val prefsHelper = SharedPreferencesHelper(getApplication())

    fun saveLoginData(token: String, email: String) {
        prefsHelper.saveSessionData(token = token, name = "Usuario", email = email)
    }

    fun logout() {
        prefsHelper.clearSession()
    }

    fun isLogged(): Boolean {
        return prefsHelper.isLoggedIn()
    }

    fun getToken(): String? {
        return prefsHelper.getSessionToken()
    }

        fun onEmailChange(newValue: String) {
        email = newValue
    }

    fun onPasswordChange(newValue: String) {
        password = newValue
    }

    fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
    }

    fun clearLoginResult() {
        loginResult = null
    }

    fun onLoginClick() {
        viewModelScope.launch {
            isLoading = true
            try {
                val request = LoginRequest(email, password)
                val response = RetrofitClient.api.login(request)

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.status == "success") {
                        val token = body.data?.token ?: ""

                        saveLoginData(token, email)
                        fetchUserProfile()

                        loginResult = "success"
                    } else {
                        loginResult = body?.message ?: "Error desconocido"
                    }
                } else {
                    val errorJson = response.errorBody()?.string()
                    val error = Gson().fromJson(errorJson, LoginResponse::class.java)
                    loginResult = error?.message ?: "Error desconocido"
                }

            } catch (e: IOException) {
                loginResult = "Error de red"
            }
            catch (e: Exception) {
                Log.d("LoginViewModel", "Error: ${e.message}")
                loginResult = e.message ?: "Error inesperado"
            } finally {
                isLoading = false
            }


        }


    }

    private suspend fun fetchUserProfile() {
        try {
            val resp = RetrofitClient.api.getProfile()
            if (resp.isSuccessful && resp.body()?.status == "success") {
                resp.body()!!.data?.let { profile ->
                    prefsHelper.saveProfileData(profile)
                }
            } else {
                Log.w("LoginVM", "Profile error: ${resp.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.e("LoginVM", "FetchProfile failed: ${e.message}")
        }
    }


}
