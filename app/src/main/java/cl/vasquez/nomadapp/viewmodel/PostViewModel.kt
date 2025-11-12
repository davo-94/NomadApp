package cl.vasquez.nomadapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.vasquez.nomadapp.data.AppDatabase
import cl.vasquez.nomadapp.data.Post
import cl.vasquez.nomadapp.model.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository
    private val _postList = MutableStateFlow<List<Post>>(emptyList())
    val postList: StateFlow<List<Post>> = _postList

    init {
        val dao = AppDatabase.getDatabase(application).postDao()
        repository = PostRepository(dao)
        loadPosts()
    }

    /**
     * Carga todas las publicaciones desde la base de datos
     */
    fun loadPosts() {
        viewModelScope.launch {
            _postList.value = repository.getAll()
        }
    }

    /**
     * Inserta una nueva publicación en la base de datos.
     * Ahora soporta múltiples imágenes por publicación.
     */
    fun addPost(title: String, description: String, date: String, imageUris: List<String>) {
        viewModelScope.launch {
            repository.insert(
                Post(
                    title = title,
                    description = description,
                    date = date,
                    imageUris = imageUris // ← guarda lista completa
                )
            )
            loadPosts()
        }
    }

    /**
     * Elimina una publicación existente
     */
    fun deletePost(post: Post) {
        viewModelScope.launch {
            repository.delete(post)
            loadPosts()
        }
    }

    fun updatePost(post: Post) {
        viewModelScope.launch {
            repository.update(post)
            loadPosts()
        }
    }

}
