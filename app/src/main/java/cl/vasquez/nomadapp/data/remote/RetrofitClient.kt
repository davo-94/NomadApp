package cl.vasquez.nomadapp.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object  RetrofitClient {

    //URL base de la api externa
    private const val BASE_URL = "https://ipapi.co/"


    //Logger para ver respuestas en Logcat
    private val logging = HttpLoggingInterceptor().apply {
        level= HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .header("User-Agent","NomadApp/1.0")
                .build()
            chain.proceed(request)
        }
        .addInterceptor(logging)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    //Instancia del servicio
    val locationApi: LocationApiService = retrofit.create(LocationApiService::class.java)
}