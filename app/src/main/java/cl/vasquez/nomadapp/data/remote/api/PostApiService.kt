package cl.vasquez.nomadapp.data.remote.api

import cl.vasquez.nomadapp.data.remote.dto.PostDto
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface PostApiService {

    @GET("posts")
    suspend fun getPosts(): List<PostDto>

    @POST("posts")
    suspend fun createPost(@Body post: PostDto): PostDto

    @Multipart
    @POST("posts/{id}/images")
    suspend fun uploadImages(
        @Path("id") postId: Long,
        @Part images: List<MultipartBody.Part>
    ): List<String>



    @PUT("posts/{id}")
    suspend fun updatePost(
        @Path("id") id: Long,
        @Body post: PostDto
    ): PostDto


    @DELETE("posts/{id}")
    suspend fun deletePost(@Path("id") id: Long)

}

