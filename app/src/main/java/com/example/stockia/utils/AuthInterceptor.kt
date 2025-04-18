package com.example.stockia.network

import com.example.stockia.utils.LogoutEventBus
import com.example.stockia.utils.SharedPreferencesHelper
import com.example.stockia.utils.isJwtExpired
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class AuthInterceptor(private val prefs: SharedPreferencesHelper) : Interceptor {

    // Rutas que NO deben tener token
    private val publicPaths = listOf(
        "/users/login",
        "/users/register",
        "/users/confirm-email",
        "/users/forgot-password",
        "/users/reset-password"
    )

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val path = request.url.encodedPath

        if (publicPaths.any { path.contains(it) }) {
            return chain.proceed(request)
        }

        val token = prefs.getSessionToken()
        if (token.isNullOrBlank() || token.isJwtExpired()) {
            prefs.clearSession()
            kotlinx.coroutines.GlobalScope.launch(Dispatchers.Main) {
                LogoutEventBus.publishLogout()
            }
            throw IOException("TOKEN_EXPIRED")

        }

        val newRequest = request.newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(newRequest)
    }
}
