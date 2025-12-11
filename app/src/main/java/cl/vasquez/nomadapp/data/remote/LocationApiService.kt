package cl.vasquez.nomadapp.data.remote

import retrofit2.http.GET

interface LocationApiService {
    //Llamará a htt:ip-api.com/json
    @GET("json/")
    suspend fun getLocation(): LocationResponse
}