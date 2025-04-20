import android.content.Context
import com.example.stockia.model.ApiService
import com.example.stockia.network.AuthInterceptor
import com.example.stockia.utils.SharedPreferencesHelper
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    fun create(context: Context): ApiService {
        val prefs = SharedPreferencesHelper(context)
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(prefs))
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://173.212.224.226:3000/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiService::class.java)
    }

    lateinit var api: ApiService
}
