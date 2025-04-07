package com.example.stockia.network

import com.example.stockia.utils.SharedPreferencesHelper
import okhttp3.Interceptor
import okhttp3.Response

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
        val originalRequest = chain.request()

        val urlPath = originalRequest.url.encodedPath

        val shouldSkipToken = publicPaths.any { path ->
            urlPath.contains(path)
        }

        val requestBuilder = originalRequest.newBuilder()

        if (!shouldSkipToken) {
            prefs.getSessionToken()?.let { token ->
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }
        }

        return chain.proceed(requestBuilder.build())
    }
}
