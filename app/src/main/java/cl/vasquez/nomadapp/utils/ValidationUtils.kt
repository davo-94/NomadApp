package cl.vasquez.nomadapp.utils

object ValidationUtils {
    fun isValidEmail(email: String): Boolean {
        if (email.isBlank()) return false
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        return emailRegex.matches(email)
    }

    fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }

    fun getEmailErrorMessage(email: String): String? {
        return when {
            email.isBlank() -> "El email es requerido"
            !isValidEmail(email) -> "Email inválido"
            else -> null
        }
    }

    fun getPasswordErrorMessage(password: String): String? {
        return when {
            password.isBlank() -> "La contraseña es requerida"
            !isValidPassword(password) -> "La contraseña debe tener al menos 6 caracteres"
            else -> null
        }
    }
}
