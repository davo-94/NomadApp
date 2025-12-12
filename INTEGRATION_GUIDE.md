# Guía de Integración Backend NomadApp

## Descripción General

Esta guía explica cómo actualizar tu aplicación Android para que consuma los servicios del backend Spring Boot en lugar de usar Room database localmente.

## Cambios Principales

### 1. Agregar Dependencias al `build.gradle.kts`

Agregar al archivo `app/build.gradle.kts`:

```kotlin
dependencies {
    // ... dependencias existentes ...
    
    // Retrofit para llamadas HTTP
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    
    // OkHttp para logging y interceptores
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    
    // DataStore para guardar JWT
    implementation("androidx.datastore:datastore-preferences:1.1.1")
}
```

### 2. Configurar API Base URL

En `MainActivity.kt` o en tu Application class:

```kotlin
override fun onCreate() {
    super.onCreate()
    
    // Inicializar el proveedor de API
    ApiProvider.initialize(applicationContext)
    
    // Configurar base URL según ambiente
    val baseUrl = if (BuildConfig.DEBUG) {
        "http://10.0.2.2:8080/api/"  // Para emulador
        // o "http://tu_ip_local:8080/api/"  // Para dispositivo físico
    } else {
        "https://api.tudominio.com/api/"  // Producción
    }
}
```

### 3. Actualizar el LoginViewModel

Cambiar `LoginViewModel.kt` para usar `RemoteLoginRepository`:

```kotlin
package cl.vasquez.nomadapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.vasquez.nomadapp.model.RemoteLoginRepository
import cl.vasquez.nomadapp.network.ApiProvider
import cl.vasquez.nomadapp.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

class LoginViewModel(
    private val tokenManager: TokenManager
) : ViewModel() {
    
    private val loginRepository = RemoteLoginRepository(
        ApiProvider.apiService,
        tokenManager
    )
    
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState
    
    fun login(username: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            val result = loginRepository.login(username, password)
            
            result.onSuccess { response ->
                _uiState.value = LoginUiState(
                    isLoggedIn = true,
                    successMessage = response.message
                )
            }
            
            result.onFailure { error ->
                _uiState.value = LoginUiState(
                    errorMessage = error.message ?: "Error en login"
                )
            }
        }
    }
    
    fun register(
        username: String,
        email: String,
        password: String,
        firstName: String,
        lastName: String
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            val result = loginRepository.register(
                username, email, password, firstName, lastName
            )
            
            result.onSuccess { response ->
                _uiState.value = LoginUiState(
                    isLoggedIn = true,
                    successMessage = response.message
                )
            }
            
            result.onFailure { error ->
                _uiState.value = LoginUiState(
                    errorMessage = error.message ?: "Error en registro"
                )
            }
        }
    }
}
```

### 4. Actualizar el PostViewModel

```kotlin
package cl.vasquez.nomadapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.vasquez.nomadapp.data.Post
import cl.vasquez.nomadapp.model.RemotePostRepository
import cl.vasquez.nomadapp.network.ApiProvider
import cl.vasquez.nomadapp.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class PostUiState(
    val posts: List<Post> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

class PostViewModel(
    private val tokenManager: TokenManager
) : ViewModel() {
    
    private val postRepository = RemotePostRepository(
        ApiProvider.apiService,
        tokenManager
    )
    
    private val _uiState = MutableStateFlow(PostUiState())
    val uiState: StateFlow<PostUiState> = _uiState
    
    init {
        loadPublicPosts()
    }
    
    fun loadPublicPosts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            val result = postRepository.getPublicPosts()
            
            result.onSuccess { posts ->
                _uiState.value = PostUiState(posts = posts)
            }
            
            result.onFailure { error ->
                _uiState.value = PostUiState(
                    errorMessage = error.message ?: "Error al cargar posts"
                )
            }
        }
    }
    
    fun createPost(title: String, content: String, imageUrl: String? = null) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            val result = postRepository.createPost(title, content, imageUrl)
            
            result.onSuccess { newPost ->
                _uiState.value = _uiState.value.copy(
                    posts = listOf(newPost) + _uiState.value.posts,
                    successMessage = "Post creado exitosamente"
                )
            }
            
            result.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    errorMessage = error.message ?: "Error al crear post"
                )
            }
        }
    }
    
    fun likePost(postId: Long) {
        viewModelScope.launch {
            val result = postRepository.likePost(postId)
            
            result.onSuccess { updatedPost ->
                _uiState.value = _uiState.value.copy(
                    posts = _uiState.value.posts.map {
                        if (it.id == postId) updatedPost else it
                    }
                )
            }
            
            result.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    errorMessage = error.message ?: "Error al dar like"
                )
            }
        }
    }
}
```

### 5. Actualizar el ContactViewModel

```kotlin
package cl.vasquez.nomadapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.vasquez.nomadapp.data.Contact
import cl.vasquez.nomadapp.model.RemoteContactRepository
import cl.vasquez.nomadapp.network.ApiProvider
import cl.vasquez.nomadapp.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ContactUiState(
    val contacts: List<Contact> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

class ContactViewModel(
    private val tokenManager: TokenManager
) : ViewModel() {
    
    private val contactRepository = RemoteContactRepository(
        ApiProvider.apiService,
        tokenManager
    )
    
    private val _uiState = MutableStateFlow(ContactUiState())
    val uiState: StateFlow<ContactUiState> = _uiState
    
    fun loadMyContacts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            val result = contactRepository.getMyContacts()
            
            result.onSuccess { contacts ->
                _uiState.value = ContactUiState(contacts = contacts)
            }
            
            result.onFailure { error ->
                _uiState.value = ContactUiState(
                    errorMessage = error.message ?: "Error al cargar contactos"
                )
            }
        }
    }
    
    fun createContact(name: String, email: String, phone: String, address: String? = null) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            val result = contactRepository.createContact(name, email, phone, address)
            
            result.onSuccess { newContact ->
                _uiState.value = _uiState.value.copy(
                    contacts = listOf(newContact) + _uiState.value.contacts,
                    successMessage = "Contacto creado exitosamente"
                )
            }
            
            result.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    errorMessage = error.message ?: "Error al crear contacto"
                )
            }
        }
    }
    
    fun deleteContact(contactId: Long) {
        viewModelScope.launch {
            val result = contactRepository.deleteContact(contactId)
            
            result.onSuccess {
                _uiState.value = _uiState.value.copy(
                    contacts = _uiState.value.contacts.filter { it.id != contactId }
                )
            }
            
            result.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    errorMessage = error.message ?: "Error al eliminar contacto"
                )
            }
        }
    }
}
```

## Control de Roles (Panel de Administrador)

### Verificar si el usuario es Admin

```kotlin
val isAdmin = tokenManager.rolesFlow.map { roles ->
    roles?.contains("ADMIN") ?: false
}.first()

if (isAdmin) {
    // Mostrar opciones de administrador
    // - Gestionar usuarios
    // - Asignar roles
    // - Deshabilitar/Habilitar usuarios
}
```

### Crear interfaz para Admin

```kotlin
// En tu navegación, agregar pantalla de admin
NavHost(navController = navController, startDestination = "home") {
    composable("home") { HomeScreen(navController) }
    composable("admin") { AdminScreen(navController) }  // Nueva pantalla
    // ... más rutas
}
```

## Manejo de Tokens

El token JWT se guarda automáticamente después del login/register y se envía en cada petición HTTP en el header `Authorization: Bearer <token>`.

Para acceder al token en cualquier parte:

```kotlin
val tokenManager = TokenManager(context)

// Obtener token actual
val token = tokenManager.tokenFlow.collectAsState().value

// Limpiar token en logout
tokenManager.clearToken()
```

## Actualizar Modelos de Datos

Tus modelos existentes en `data/` deben mantener la compatibilidad con los DTOs del backend:

```kotlin
// En data/User.kt
@Entity
data class User(
    @PrimaryKey val id: Long,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val profileImageUrl: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now()
)

// En data/Post.kt
@Entity
data class Post(
    @PrimaryKey val id: Long,
    val title: String,
    val content: String,
    val imageUrl: String? = null,
    val authorId: Long,
    val likes: Int = 0,
    val published: Boolean = true,
    val createdAt: LocalDateTime = LocalDateTime.now()
)

// En data/Contact.kt
@Entity
data class Contact(
    @PrimaryKey val id: Long,
    val name: String,
    val email: String,
    val phone: String,
    val address: String? = null,
    val userId: Long,
    val createdAt: LocalDateTime = LocalDateTime.now()
)
```

## Monitoreo de Errores

Agregar un interceptor para manejar errores comunes:

```kotlin
private val errorInterceptor = object : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        return try {
            val response = chain.proceed(request)
            when (response.code) {
                401 -> {
                    // Token expirado, limpiar sesión
                    runBlocking { tokenManager.clearToken() }
                }
                403 -> {
                    // Acceso denegado
                }
                404 -> {
                    // Recurso no encontrado
                }
            }
            response
        } catch (e: Exception) {
            throw e
        }
    }
}
```

## Pruebas

### Probar Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

### Probar Crear Post (Requiere Auth)

```bash
curl -X POST http://localhost:8080/api/posts \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN_AQUI" \
  -d '{
    "title": "Mi primer post",
    "content": "Contenido del post...",
    "published": true
  }'
```

## Próximos Pasos

1. ✅ Backend API creado
2. ⏳ Actualizar LocalLoginRepository → RemoteLoginRepository
3. ⏳ Actualizar LocalPostRepository → RemotePostRepository
4. ⏳ Crear pantalla de administración
5. ⏳ Manejar offline mode (caché con Room)
6. ⏳ Agregar paginación en posts
7. ⏳ Tests de integración

## Solución de Problemas

### Error de conexión al backend

**Problema:** `java.net.ConnectException: Failed to connect to 10.0.2.2:8080`

**Solución:**
- En emulador: usar `10.0.2.2` en lugar de `localhost`
- En dispositivo físico: usar la IP local de tu máquina (ej: `192.168.1.100`)

### Token expirado

**Problema:** Error 401 Unauthorized después de 24 horas

**Solución:**
- Implementar refresh token mechanism
- Limpiar sesión y redirigir a login

### CORS errors

**Problema:** Error de CORS en desarrollo

**Solución:**
- El backend ya tiene configurado: `@CrossOrigin(origins = "*", maxAge = 3600)`
- En producción, restringir a dominio específico

## Referencias

- [Documentación de Retrofit](https://square.github.io/retrofit/)
- [OkHttp Interceptors](https://square.github.io/okhttp/interceptors/)
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
- [DataStore](https://developer.android.com/topic/libraries/architecture/datastore)

