package cl.vasquez.nomadapp.data.remote.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NomadApiClient {

    private const val BASE_URL = "http://192.168.0.102:8082/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val postApi: PostApiService by lazy {
        retrofit.create(PostApiService::class.java)
    }

    //Contact API
    val contactApi: ContactApiService by lazy {
        retrofit.create(ContactApiService::class.java)
    }
}
