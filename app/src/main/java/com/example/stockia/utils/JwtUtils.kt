package com.example.stockia.utils

import com.auth0.android.jwt.JWT
import java.util.Date

fun String.isJwtExpired(): Boolean {
    return try {
        val jwt = JWT(this)
        val expiresAt: Date? = jwt.expiresAt
        expiresAt?.before(Date()) ?: true
    } catch (e: Exception) {
        true
    }
}
