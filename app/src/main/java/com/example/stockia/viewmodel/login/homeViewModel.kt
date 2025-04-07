import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockia.model.ConfirmEmailResponse
import com.example.stockia.utils.SharedPreferencesHelper
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.Response

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    var isLoading by mutableStateOf(false)
        private set

    var resultMessage by mutableStateOf<String?>(null)
        private set

    private val context = getApplication<Application>()
    private val prefsHelper = SharedPreferencesHelper(context)

    fun onLogoutClick() {
        viewModelScope.launch {
            isLoading = true
            try {
                val response: Response<ConfirmEmailResponse> = RetrofitClient.api.logout()

                if (response.isSuccessful) {
                    prefsHelper.clearSession()
                    resultMessage = "success"
                } else {
                    val errorJson = response.errorBody()?.string()
                    val error = Gson().fromJson(errorJson, ConfirmEmailResponse::class.java)
                    resultMessage = error?.message ?: "Error al cerrar sesión"
                }
            } catch (e: Exception) {
                resultMessage = e.message ?: "Error inesperado"
            } finally {
                isLoading = false
            }
        }
    }

    fun clearMessage() {
        resultMessage = null
    }
}
