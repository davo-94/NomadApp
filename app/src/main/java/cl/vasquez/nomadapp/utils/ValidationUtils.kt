package cl.vasquez.nomadapp.utils

/**
 * Object (singleton) que agrupa todas las funciones de validación para formularios utilizados.
 * Adaptado del modelo TiendApp (semana 8)
 */

object ValidationUtils {

    //Nombre
    fun isValidNombre(nombre: String): Boolean {
        if (nombre.isBlank()) return false
        if (nombre.length < 2 || nombre.length > 50) return false
        val nombreRegex = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ][a-zA-ZáéíóúÁÉÍÓÚñÑ ]*$".toRegex()
        return nombreRegex.matches(nombre)
    }

    fun getNombreErrorMessage(nombre: String): String? {
        return when {
            nombre.isBlank() -> "El nombre es requerido"
            nombre.length < 2 -> "El nombre debe tener al menos 2 caracteres"
            nombre.length > 50 -> "El nombre no puede exceder 50 caracteres"
            !isValidNombre(nombre) -> "El nombre solo puede contener letras y espacios"
            else -> null
        }
    }

    //Email
    fun isValidEmail(email: String): Boolean {
        if (email.isBlank()) return false
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        return emailRegex.matches(email)
    }

    fun getEmailErrorMessage(email: String): String? {
        return when {
            email.isBlank() -> "El correo electrónico es requerido"
            !isValidEmail(email) -> "Correo electrónico inválido"
            else -> null
        }
    }

    //Mensaje
    fun isValidMensaje(mensaje: String): Boolean {
        return mensaje.isNotBlank() && mensaje.length <= 300
    }

    fun getMensajeErrorMessage(mensaje: String): String? {
        return when {
            mensaje.isBlank() -> "El mensaje es requerido"
            mensaje.length > 300 -> "El mensaje no puede exceder 300 caracteres"
            else -> null
        }
    }

    //Contraseña (Login / Registro)
    fun isValidPassword(password: String): Boolean {
        if (password.isBlank()) return false
        if (password.length < 6) return false
        // Debe contener letras y números
        val hasLetter = password.any { it.isLetter() }
        val hasDigit = password.any { it.isDigit() }
        return hasLetter && hasDigit
    }

    fun getPasswordErrorMessage(password: String): String? {
        return when {
            password.isBlank() -> "La contraseña no puede estar vacía"
            password.length < 6 -> "Debe tener al menos 6 caracteres"
            !password.any { it.isDigit() } -> "Debe contener al menos un número"
            !password.any { it.isLetter() } -> "Debe contener letras"
            else -> null
        }
    }
}
