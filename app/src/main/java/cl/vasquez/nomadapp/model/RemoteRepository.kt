package cl.vasquez.nomadapp.model

import cl.vasquez.nomadapp.network.*
import cl.vasquez.nomadapp.utils.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

/**
 * Repositorio para operaciones de autenticación con el backend
 */
class RemoteLoginRepository(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) {
    
    suspend fun login(username: String, password: String): Result<AuthResponse> =
        withContext(Dispatchers.IO) {
            try {
                val request = LoginRequest(username, password)
                val response = apiService.login(request)
                
                // Guardar token
                tokenManager.saveToken(
                    response.token,
                    response.id,
                    response.username,
                    response.roles
                )
                
                Result.success(response)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    
    suspend fun register(
        username: String,
        email: String,
        password: String,
        firstName: String,
        lastName: String
    ): Result<AuthResponse> = withContext(Dispatchers.IO) {
        try {
            val request = RegisterRequest(
                username = username,
                email = email,
                password = password,
                firstName = firstName,
                lastName = lastName
            )
            val response = apiService.register(request)
            
            // Guardar token después del registro
            tokenManager.saveToken(
                response.token,
                response.id,
                response.username,
                response.roles
            )
            
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun logout() {
        tokenManager.clearToken()
    }
    
    suspend fun isLoggedIn(): Boolean {
        return tokenManager.getToken() != null
    }
}

/**
 * Repositorio para operaciones con Posts del backend
 */
class RemotePostRepository(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) {
    
    suspend fun getPublicPosts(): Result<List<PostResponse>> =
        withContext(Dispatchers.IO) {
            try {
                val posts = apiService.getPublicPosts()
                Result.success(posts)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    
    suspend fun getPostById(postId: Long): Result<PostResponse> =
        withContext(Dispatchers.IO) {
            try {
                val post = apiService.getPostById(postId)
                Result.success(post)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    
    suspend fun createPost(title: String, content: String, imageUrl: String? = null): Result<PostResponse> =
        withContext(Dispatchers.IO) {
            try {
                val request = PostRequest(
                    title = title,
                    content = content,
                    imageUrl = imageUrl,
                    published = true
                )
                val post = apiService.createPost(request)
                Result.success(post)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    
    suspend fun updatePost(postId: Long, title: String, content: String, imageUrl: String? = null): Result<PostResponse> =
        withContext(Dispatchers.IO) {
            try {
                val request = PostRequest(
                    title = title,
                    content = content,
                    imageUrl = imageUrl
                )
                val post = apiService.updatePost(postId, request)
                Result.success(post)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    
    suspend fun deletePost(postId: Long): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                apiService.deletePost(postId)
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    
    suspend fun likePost(postId: Long): Result<PostResponse> =
        withContext(Dispatchers.IO) {
            try {
                val post = apiService.likePost(postId)
                Result.success(post)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}

/**
 * Repositorio para operaciones con Contactos del backend
 */
class RemoteContactRepository(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) {
    
    suspend fun getMyContacts(): Result<List<ContactResponse>> =
        withContext(Dispatchers.IO) {
            try {
                val contacts = apiService.getMyContacts()
                Result.success(contacts)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    
    suspend fun getContactById(contactId: Long): Result<ContactResponse> =
        withContext(Dispatchers.IO) {
            try {
                val contact = apiService.getContactById(contactId)
                Result.success(contact)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    
    suspend fun createContact(name: String, email: String, phone: String, address: String? = null): Result<ContactResponse> =
        withContext(Dispatchers.IO) {
            try {
                val request = ContactRequest(
                    name = name,
                    email = email,
                    phone = phone,
                    address = address
                )
                val contact = apiService.createContact(request)
                Result.success(contact)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    
    suspend fun updateContact(contactId: Long, name: String, email: String, phone: String, address: String? = null): Result<ContactResponse> =
        withContext(Dispatchers.IO) {
            try {
                val request = ContactRequest(
                    name = name,
                    email = email,
                    phone = phone,
                    address = address
                )
                val contact = apiService.updateContact(contactId, request)
                Result.success(contact)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    
    suspend fun deleteContact(contactId: Long): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                apiService.deleteContact(contactId)
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}