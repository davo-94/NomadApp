package cl.vasquez.nomadapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.vasquez.nomadapp.data.User
import cl.vasquez.nomadapp.data.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class UserItem(
    val id: Long,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val roles: String,
    val enabled: Boolean
)

class AdminViewModel(
    private val database: AppDatabase? = null
) : ViewModel() {
    
    private val _database = database
    
    private val _users = MutableStateFlow<List<UserItem>>(emptyList())
    val users = _users.asStateFlow()
    
    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()
    
    private val _success = MutableStateFlow<String?>(null)
    val success = _success.asStateFlow()
    
    init {
        loadUsers()
    }
    
    fun loadUsers() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            
            // Pequeño delay para permitir que se prepoblen los datos de prueba
            kotlinx.coroutines.delay(500)
            
            // Carga desde BD local únicamente
            loadUsersFromDatabase()
            
            _loading.value = false
        }
    }
    
    private suspend fun loadUsersFromDatabase() {
        withContext(Dispatchers.IO) {
            try {
                val dbUsers = _database?.userDao()?.getAllUsers()?.first() ?: emptyList()
                _users.value = dbUsers.map { user ->
                    UserItem(
                        id = user.id.toLong(),
                        username = user.username.ifEmpty { user.email.split("@")[0] },
                        email = user.email,
                        firstName = user.firstName.ifEmpty { user.email.split("@")[0] },
                        lastName = user.lastName,
                        roles = user.role,
                        enabled = user.enabled
                    )
                }
            } catch (e: Exception) {
                // Si no hay BD, mantén lista vacía
            }
        }
    }
    
    fun updateUserRole(userId: Long, newRole: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                // Actualiza solo localmente en BD
                val currentUser = _users.value.find { it.id == userId }
                if (currentUser != null) {
                    val user = User(
                        id = currentUser.id.toInt(),
                        username = currentUser.username,
                        email = currentUser.email,
                        password = "", // No cambiar password
                        firstName = currentUser.firstName,
                        lastName = currentUser.lastName,
                        role = newRole,
                        enabled = currentUser.enabled
                    )
                    withContext(Dispatchers.IO) {
                        _database?.userDao()?.update(user)
                    }
                    
                    // Actualiza UI
                    val updatedUsers = _users.value.map { userItem ->
                        if (userItem.id == userId) {
                            userItem.copy(roles = newRole)
                        } else {
                            userItem
                        }
                    }
                    _users.value = updatedUsers
                }
                
                _success.value = "Rol actualizado exitosamente"
                _loading.value = false
            } catch (e: Exception) {
                _loading.value = false
                _error.value = "Error al actualizar rol: ${e.message}"
            }
        }
    }
    
    fun enableDisableUser(userId: Long, enabled: Boolean) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                // Actualiza solo localmente en BD
                val currentUser = _users.value.find { it.id == userId }
                if (currentUser != null) {
                    val user = User(
                        id = currentUser.id.toInt(),
                        username = currentUser.username,
                        email = currentUser.email,
                        password = "", // No cambiar password
                        firstName = currentUser.firstName,
                        lastName = currentUser.lastName,
                        role = currentUser.roles,
                        enabled = enabled
                    )
                    withContext(Dispatchers.IO) {
                        _database?.userDao()?.update(user)
                    }
                    
                    // Actualiza UI
                    val updatedUsers = _users.value.map { userItem ->
                        if (userItem.id == userId) {
                            userItem.copy(enabled = enabled)
                        } else {
                            userItem
                        }
                    }
                    _users.value = updatedUsers
                }
                
                _success.value = if (enabled) "Usuario habilitado" else "Usuario deshabilitado"
                _loading.value = false
            } catch (e: Exception) {
                _loading.value = false
                _error.value = "Error al cambiar estado: ${e.message}"
            }
        }
    }
    
    fun deleteUser(userId: Long) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                // Elimina solo de BD local
                val userToDelete = _users.value.find { it.id == userId }
                if (userToDelete != null) {
                    val user = User(
                        id = userToDelete.id.toInt(),
                        username = userToDelete.username,
                        email = userToDelete.email,
                        password = "", // No cambiar password
                        firstName = userToDelete.firstName,
                        lastName = userToDelete.lastName,
                        role = userToDelete.roles,
                        enabled = userToDelete.enabled
                    )
                    withContext(Dispatchers.IO) {
                        _database?.userDao()?.delete(user)
                    }
                    
                    // Actualiza UI
                    _users.value = _users.value.filter { it.id != userId }
                }
                
                _success.value = "Usuario eliminado"
                _loading.value = false
            } catch (e: Exception) {
                _loading.value = false
                _error.value = "Error al eliminar usuario: ${e.message}"
                // Recarga desde BD en caso de error
                loadUsers()
            }
        }
    }
    
    fun clearMessages() {
        _error.value = null
        _success.value = null
    }
}
