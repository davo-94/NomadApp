package cl.vasquez.nomadapp.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

// Request que enviamos al backend
data class ContactRequest(
    val nombre: String,
    val correo: String,
    val pais: String,
    val mensaje: String
)

interface ContactApiService {

    @POST("/api/contact/send")
    suspend fun sendContact(
        @Body request: ContactRequest
    ): Response<Unit>
}
