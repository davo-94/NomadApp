package cl.vasquez.nomadapp.model

import cl.vasquez.nomadapp.data.UserDao
import cl.vasquez.nomadapp.data.User
import kotlinx.coroutines.delay

sealed class LoginResult {
    data class Success(val user: User): LoginResult()
    data class Error(val message: String): LoginResult()
    object Loading: LoginResult()
    object Idle: LoginResult()
}

class LoginRepository(private val userDao: UserDao) {

    /**
     * Intenta autenticar contra la base de datos local (Room)
     */
    suspend fun login(email: String, password: String): LoginResult {
        return try {
            // Simulamos pequeño delay (igual que en el ejemplo)
            delay(800)
            val user = userDao.getByEmail(email)
            if (user != null && user.password == password) {
                LoginResult.Success(user)
            } else {
                LoginResult.Error("Email o contraseña incorrectos")
            }
        } catch (e: Exception) {
            LoginResult.Error("Error al iniciar sesión: ${e.message}")
        }
    }
}
