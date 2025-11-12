package cl.vasquez.nomadapp.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val DATASTORE_NAME = "session_preferences"
private val Context.dataStore by preferencesDataStore(name = DATASTORE_NAME)

/**
 * Object que gestiona la sesión del usuario mediante DataStore.
 * Corregido para evitar errores de inicialización del contexto.
 */
object SessionManager {

    // Contexto de la aplicación (nullable para evitar crash si no se inicializa)
    private var context: Context? = null

    // Inicializa el contexto desde MainActivity
    fun initialize(appContext: Context) {
        context = appContext.applicationContext
    }

    // Verifica que el contexto esté inicializado antes de usarlo
    private fun requireContext(): Context {
        return context ?: throw IllegalStateException(
            "SessionManager no ha sido inicializado. Llama a initialize(context) en MainActivity.onCreate()."
        )
    }

    private val USER_EMAIL = stringPreferencesKey("user_email")
    private val USER_ROLE = stringPreferencesKey("user_role")

    // Guardar sesión del usuario después de login exitoso
    suspend fun saveUserSession(email: String, role: String) {
        requireContext().dataStore.edit { preferences ->
            preferences[USER_EMAIL] = email
            preferences[USER_ROLE] = role
        }
    }

    // Obtener email del usuario logueado (Flow para reactivity)
    fun getUserEmail(): Flow<String?> = requireContext().dataStore.data.map { preferences ->
        preferences[USER_EMAIL]
    }

    // Obtener rol del usuario logueado
    fun getUserRole(): Flow<String?> = requireContext().dataStore.data.map { preferences ->
        preferences[USER_ROLE]
    }

    // Cerrar sesión
    suspend fun logout() {
        requireContext().dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    // Verificar si hay sesión activa
    fun hasActiveSession(): Flow<Boolean> = requireContext().dataStore.data.map { preferences ->
        preferences[USER_EMAIL] != null
    }
}
