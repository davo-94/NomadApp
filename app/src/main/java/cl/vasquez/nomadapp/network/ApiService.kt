package cl.vasquez.nomadapp.network

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * Interfaz de Retrofit para definir los endpoints de la API
 */
interface ApiService {

    // ========== AUTENTICACIÓN ==========
    
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse
    
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse
    
    // ========== POSTS ==========
    
    @GET("posts")
    suspend fun getPublicPosts(): List<PostResponse>
    
    @GET("posts/{id}")
    suspend fun getPostById(@Path("id") postId: Long): PostResponse
    
    @POST("posts")
    suspend fun createPost(@Body request: PostRequest): PostResponse
    
    @PUT("posts/{id}")
    suspend fun updatePost(
        @Path("id") postId: Long,
        @Body request: PostRequest
    ): PostResponse
    
    @DELETE("posts/{id}")
    suspend fun deletePost(@Path("id") postId: Long)
    
    @POST("posts/{id}/like")
    suspend fun likePost(@Path("id") postId: Long): PostResponse
    
    // ========== CONTACTOS ==========
    
    @GET("contacts")
    suspend fun getMyContacts(): List<ContactResponse>
    
    @GET("contacts/{id}")
    suspend fun getContactById(@Path("id") contactId: Long): ContactResponse
    
    @POST("contacts")
    suspend fun createContact(@Body request: ContactRequest): ContactResponse
    
    @PUT("contacts/{id}")
    suspend fun updateContact(
        @Path("id") contactId: Long,
        @Body request: ContactRequest
    ): ContactResponse
    
    @DELETE("contacts/{id}")
    suspend fun deleteContact(@Path("id") contactId: Long)
}

// DTOs Request
data class LoginRequest(
    val username: String,
    val password: String
)

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String
)

data class PostRequest(
    val title: String,
    val content: String,
    val imageUrl: String? = null,
    val published: Boolean = true
)

data class ContactRequest(
    val name: String,
    val email: String,
    val phone: String,
    val address: String? = null
)

// DTOs Response
data class AuthResponse(
    val token: String,
    val type: String = "Bearer",
    val id: Long,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val roles: String,
    val message: String
)

data class PostResponse(
    val id: Long,
    val title: String,
    val content: String,
    val imageUrl: String? = null,
    val likes: Int = 0,
    val published: Boolean = true,
    val createdAt: String,
    val updatedAt: String
)

data class ContactResponse(
    val id: Long,
    val name: String,
    val email: String,
    val phone: String,
    val address: String? = null,
    val createdAt: String,
    val updatedAt: String
)
