package cl.vasquez.nomadapp.network

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

private const val API_BASE_URL = "http://localhost:8080/api/"
private const val DATASTORE_NAME = "auth_preferences"
private val TOKEN_PREFERENCE_KEY = stringPreferencesKey("auth_token")

/**
 * Proveedor de servicio API que configura Retrofit con autenticación JWT
 */
object ApiProvider {
    
    private lateinit var context: Context
    private val dataStore: DataStore<Preferences> by lazy { context.preferencesDataStore }
    
    private val authInterceptor = object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            
            // Obtener token del DataStore
            val token = runBlocking {
                dataStore.data.first()[TOKEN_PREFERENCE_KEY]
            }
            
            return if (token != null) {
                val newRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer $token")
                    .build()
                chain.proceed(newRequest)
            } else {
                chain.proceed(originalRequest)
            }
        }
    }
    
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()
    
    private val retrofit = Retrofit.Builder()
        .baseUrl(API_BASE_URL)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
    
    fun initialize(applicationContext: Context) {
        this.context = applicationContext
    }
}

// Extensión para DataStore
val Context.preferencesDataStore: DataStore<Preferences> by preferencesDataStore(name = DATASTORE_NAME)
