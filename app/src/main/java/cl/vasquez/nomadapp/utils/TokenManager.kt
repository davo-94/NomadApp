package cl.vasquez.nomadapp.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private const val TOKEN_STORE_NAME = "auth_store"
private val TOKEN_KEY = stringPreferencesKey("auth_token")
private val USER_ID_KEY = stringPreferencesKey("user_id")
private val USERNAME_KEY = stringPreferencesKey("username")
private val USER_ROLES_KEY = stringPreferencesKey("user_roles")

val Context.authDataStore by preferencesDataStore(name = TOKEN_STORE_NAME)

/**
 * Gestor de tokens de autenticación usando DataStore
 */
class TokenManager(private val context: Context) {
    
    val tokenFlow: Flow<String?> = context.authDataStore.data.map { preferences ->
        preferences[TOKEN_KEY]
    }
    
    val userIdFlow: Flow<Long?> = context.authDataStore.data.map { preferences ->
        preferences[USER_ID_KEY]?.toLongOrNull()
    }
    
    val usernameFlow: Flow<String?> = context.authDataStore.data.map { preferences ->
        preferences[USERNAME_KEY]
    }
    
    val rolesFlow: Flow<String?> = context.authDataStore.data.map { preferences ->
        preferences[USER_ROLES_KEY]
    }
    
    suspend fun saveToken(token: String, userId: Long, username: String, roles: String) {
        context.authDataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
            preferences[USER_ID_KEY] = userId.toString()
            preferences[USERNAME_KEY] = username
            preferences[USER_ROLES_KEY] = roles
        }
    }
    
    suspend fun clearToken() {
        context.authDataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
            preferences.remove(USER_ID_KEY)
            preferences.remove(USERNAME_KEY)
            preferences.remove(USER_ROLES_KEY)
        }
    }
    
    suspend fun getToken(): String? {
        return context.authDataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }.first()
    }
    
    suspend fun getUserId(): Long? {
        return context.authDataStore.data.map { preferences ->
            preferences[USER_ID_KEY]?.toLongOrNull()
        }.first()
    }
    
    suspend fun isAdmin(): Boolean {
        return context.authDataStore.data.map { preferences ->
            preferences[USER_ROLES_KEY]?.contains("ADMIN") ?: false
        }.first()
    }
}