package cl.vasquez.nomadapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.vasquez.nomadapp.data.remote.api.NomadApiClient
import cl.vasquez.nomadapp.data.remote.dto.PostDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


class PostViewModel : ViewModel() {

    private val _posts = MutableStateFlow<List<PostDto>>(emptyList())
    val posts: StateFlow<List<PostDto>> = _posts

    fun createPost(post: PostDto, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                NomadApiClient.postApi.createPost(post)
                loadPosts()
                onSuccess()
            } catch (e: Exception) {
                android.util.Log.e(
                    "POST_CREATE_ERROR",
                    "Error al crear post: ${e.message}",
                    e
                )
            }
        }
    }

    fun createPostWithImages(
        post: PostDto,
        imageUris: List<Uri>,
        context: Context,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                // Crear post
                val createdPost = NomadApiClient.postApi.createPost(post)

                //Preparar imágenes(multipart)
                if (imageUris.isNotEmpty()) {
                    val parts = imageUris.map { uri ->
                        val inputStream = context.contentResolver.openInputStream(uri)
                            ?: throw IllegalStateException("No se pudo abrir la imagen")

                        val file = File.createTempFile("img_", ".jpg", context.cacheDir)
                        file.outputStream().use { output ->
                            inputStream.copyTo(output)
                        }

                        val requestBody =
                            file.asRequestBody("image/*".toMediaTypeOrNull())

                        MultipartBody.Part.createFormData(
                            "files",
                            file.name,
                            requestBody
                        )
                    }

                    android.util.Log.d(
                        "UPLOAD_IMAGES",
                        "Subiendo ${parts.size} imagenes al post ${createdPost.id}"
                    )
                    NomadApiClient.postApi.uploadImages(createdPost.id!!, parts)
                }

                //Refrescar lista
                loadPosts()
                onSuccess()
            } catch (e: Exception) {
                android.util.Log.e(
                    "POST_CREATE_WITH_IMAGES",
                    "Error al crear post: ${e.message}",
                    e
                )
            }
        }
    }

    fun loadPosts() {
        viewModelScope.launch {
            try {
                _posts.value = NomadApiClient.postApi.getPosts()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updatePost(id: Long, post: PostDto, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                NomadApiClient.postApi.updatePost(id, post)
                loadPosts() // refresca lista
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deletePost(id: Long, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                NomadApiClient.postApi.deletePost(id)
                loadPosts() // refresca lista desde backend
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
