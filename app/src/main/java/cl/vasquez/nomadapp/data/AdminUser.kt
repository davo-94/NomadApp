package cl.vasquez.nomadapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad para almacenar usuarios en la BD local para administración
 * Sincroniza con el servidor cuando hay cambios
 */
@Entity(tableName = "admin_users")
data class AdminUser(
    @PrimaryKey val id: Long,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val roles: String, // Almacena roles como string (ej: "ADMIN", "USER", "GUEST")
    val enabled: Boolean,
    val syncPending: Boolean = false // Indica si hay cambios pendientes por enviar al servidor
)
