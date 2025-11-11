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

class PostViewModel(application: Application): AndroidViewModel(application) {
    private val repository: PostRepository
    private val _postList = MutableStateFlow<List<Post>>(emptyList())
    val postList: StateFlow<List<Post>> = _postList

    init {
        val dao = AppDatabase.getDatabase(application).postDao()
        repository = PostRepository(dao)
        loadPosts()
    }

    fun loadPosts() {
        viewModelScope.launch {
            _postList.value = repository.getAll()
        }
    }

    fun addPost(title: String, description: String, date: String, imageUri: String?) {
        viewModelScope.launch {
            repository.insert(Post(title = title, description = description, date = date, imageUri = imageUri))
        loadPosts()
        }
    }

    fun deletePost(post: Post) {
        viewModelScope.launch {
            repository.delete(post)
            loadPosts()
        }
    }

}