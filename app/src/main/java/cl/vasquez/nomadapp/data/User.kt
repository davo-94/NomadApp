package cl.vasquez.nomadapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val email: String,
    val password: String,
    val role: String = "USER",
    val username: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val enabled: Boolean = true
)
