package com.example.stockia.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.stockia.model.ProfileData

class SharedPreferencesHelper(context: Context) {

    private val sharedPref: SharedPreferences =
        context.getSharedPreferences("stockiaPrefs", Context.MODE_PRIVATE)

    // ============================= SESIÓN ============================= //

    fun saveSessionData(token: String, name: String, email: String) {
        with(sharedPref.edit()) {
            putString("session_token", token)
            putString("user_name", name)
            putString("user_email", email)
            apply()
        }
    }

    fun getSessionToken(): String? {
        return sharedPref.getString("session_token", null)
    }

    fun getUserName(): String {
        return sharedPref.getString("user_name", "Usuario") ?: "Usuario"
    }

    fun getUserEmail(): String {
        return sharedPref.getString("user_email", "") ?: ""
    }

    fun isLoggedIn(): Boolean {
        return getSessionToken() != null
    }

    fun clearSession() {
        with(sharedPref.edit()) {
            remove("session_token")
            remove("user_name")
            remove("user_email")
            apply()
        }
    }

    fun saveProfileData(profile: ProfileData) {
        with(sharedPref.edit()) {
            putString("user_full_name", profile.fullName)
            putString("user_company_name", profile.companyName)
            putString("user_phone", profile.phone)
            apply()
        }
    }

    fun getCompanyName(): String =
        sharedPref.getString("user_company_name", "") ?: ""

    // ============================= VERIFICACIÓN ============================= //

    fun setVerificationStatus(isVerified: Boolean) {
        with(sharedPref.edit()) {
            putBoolean("is_verified", isVerified)
            apply()
        }
    }

    fun isVerified(): Boolean {
        return sharedPref.getBoolean("is_verified", false)
    }


}
