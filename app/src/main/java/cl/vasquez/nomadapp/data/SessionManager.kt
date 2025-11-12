package cl.vasquez.nomadapp.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val DATASTORE_NAME = "session_preferences"
private val Context.dataStore by preferencesDataStore(name = DATASTORE_NAME)

object SessionManager {
    private lateinit var context: Context
    
    fun initialize(appContext: Context) {
        context = appContext
    }
    
    private val USER_EMAIL = stringPreferencesKey("user_email")
    private val USER_ROLE = stringPreferencesKey("user_role")
    
    // Guardar sesión del usuario después de login exitoso
    suspend fun saveUserSession(email: String, role: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_EMAIL] = email
            preferences[USER_ROLE] = role
        }
    }
    
    // Obtener email del usuario logueado (Flow para reactivity)
    fun getUserEmail(): Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_EMAIL]
    }
    
    // Obtener rol del usuario logueado
    fun getUserRole(): Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_ROLE]
    }
    
    // Cerrar sesión
    suspend fun logout() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
    
    // Verificar si hay sesión activa
    fun hasActiveSession(): Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[USER_EMAIL] != null
    }
}
