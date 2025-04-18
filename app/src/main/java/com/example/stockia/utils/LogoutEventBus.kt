package com.example.stockia.utils

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object LogoutEventBus {
    // emisor interno
    private val _events = MutableSharedFlow<Unit>(replay = 0)
    // expositor sólo lectura
    val events = _events.asSharedFlow()

    // llamada desde el Interceptor
    suspend fun publishLogout() {
        _events.emit(Unit)
    }
}