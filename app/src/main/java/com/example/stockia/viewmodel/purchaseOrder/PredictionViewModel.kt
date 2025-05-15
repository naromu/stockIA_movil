package com.example.stockia.viewmodel.purchaseOrder

import com.example.stockia.model.Prediction
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockia.model.ConfirmedSale
import com.example.stockia.model.PredictRequest
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PredictionViewModel : ViewModel() {

    companion object {
        private const val TAG = "PredictionViewModel"
    }

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _predictions = MutableStateFlow<List<Prediction>>(emptyList())
    val predictions: StateFlow<List<Prediction>> = _predictions

    private val _productsMap = MutableStateFlow<Map<Int, String>>(emptyMap())
    val productsMap: StateFlow<Map<Int, String>> = _productsMap

    init {
        fetchPredictions()
    }

    private fun fetchPredictions() {
        viewModelScope.launch {
            _isLoading.value = true
            Log.d(TAG, "🔄 Iniciando fetchPredictions()")
            try {
                // 1) confirmed-sales
                Log.d(TAG, "1) Llamando a getConfirmedSales()")
                val csResp = RetrofitClient.api.getConfirmedSales()
                Log.d(TAG, " → Código: ${csResp.code()}, éxito: ${csResp.isSuccessful}")
                val csBody = csResp.body()
                Log.d(TAG, " → Body status=${csBody?.status}, message='${csBody?.message}'")
                if (!csResp.isSuccessful || csBody?.status != "success") {
                    val err = csBody?.message ?: "Error al traer ventas confirmadas"
                    Log.d(TAG, " ❌ confirmed-sales error: $err")
                    throw Exception(err)
                }
                val confirmed: List<ConfirmedSale> = csBody.data
                Log.d(TAG, " → ConfirmedSales: tamaño=${confirmed.size}")

                // 2) products
                Log.d(TAG, "2) Llamando a getProducts()")
                val pResp = RetrofitClient.api.getProducts()
                Log.d(TAG, " → Código: ${pResp.code()}, éxito: ${pResp.isSuccessful}")
                val pBody = pResp.body()
                Log.d(TAG, " → Body status=${pBody?.status}, message='${pBody?.message}'")
                if (!pResp.isSuccessful || pBody?.status != "success") {
                    val err = pBody?.message ?: "Error al traer productos"
                    Log.d(TAG, " ❌ products error: $err")
                    throw Exception(err)
                }
                val products = pBody.data
                Log.d(TAG, " → Productos: tamaño=${products.size}")

                // Guardamos mapa id->name para UI
                _productsMap.value = products.associate { it.id to it.name }

                // Filtrar solo los confirmed existentes
                val filtered = confirmed.filter { cs ->
                    _productsMap.value.containsKey(cs.productId)
                }
                Log.d(TAG, " → Filtrados (existentes): tamaño=${filtered.size} / ${confirmed.size}")

                if (filtered.isEmpty()) {
                    Log.d(TAG, "⚠️ No hay ventas para analizar, omitiendo pedido de predicción.")
                    _predictions.value = emptyList()
                    _error.value = null
                    return@launch
                }

                // 3) predict
                val predictUrl = "http://173.212.224.226:8080/predict-multiple-shortages"
                Log.d(TAG, "3) Preparando PredictRequest con ${filtered.size} elementos")
                val reqBody = PredictRequest(
                    status = "",
                    message = "",
                    data = filtered
                )
                Log.d(TAG, " → JSON body: ${Gson().toJson(reqBody)}")

                Log.d(TAG, "3) Llamando a predictMultipleShortages() en $predictUrl")
                val prResp = RetrofitClient.api.predictMultipleShortages(predictUrl, reqBody)
                Log.d(TAG, " → Código: ${prResp.code()}, éxito: ${prResp.isSuccessful}")
                prResp.errorBody()?.string()?.let { raw ->
                    Log.d(TAG, " → errorBody raw: $raw")
                }
                val prBody = prResp.body()
                Log.d(TAG, " → Body status=${prBody?.status}, message='${prBody?.message}'")
                if (!prResp.isSuccessful || prBody?.status != "success") {
                    val err = prBody?.message ?: "Error en predicción IA"
                    Log.d(TAG, " ❌ predict error: $err")
                    throw Exception(err)
                }

                _predictions.value = prBody.data
                _error.value = null
                Log.d(TAG, "✅ fetchPredictions() completado sin errores")

            } catch (e: Exception) {
                Log.d(TAG, "💥 Excepción en fetchPredictions: ${e.message}", e)
                _error.value = e.message
            } finally {
                _isLoading.value = false
                Log.d(TAG, "🔄 fetchPredictions() terminó (isLoading=false)")
            }
        }
    }
}