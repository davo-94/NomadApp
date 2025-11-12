package cl.vasquez.nomadapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Actualizamos la base de datos para incluir la entidad User.
 * Incrementamos la versión a 2 y añadimos fallbackToDestructiveMigration
 * para simplificar durante el desarrollo.
 */
@Database(entities = [Post::class, User::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun userDao(): UserDao

    companion object{
        /**
         * Patrón singleton asegura que la base de datos se cree una sola
         * vez y esté accesible desde toda la app.
         */
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "nomadapp_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance

                // Pre-popular usuarios de prueba si no existen (solo en desarrollo)
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val dao = instance.userDao()
                        // admin
                        if (dao.getByEmail("admin@nomadapp.com") == null) {
                            dao.insert(User(email = "admin@nomadapp.com", password = "123456", role = "admin"))
                        }
                        // usuario base / invitado
                        if (dao.getByEmail("user@nomadapp.com") == null) {
                            dao.insert(User(email = "user@nomadapp.com", password = "password", role = "guest"))
                        }
                    } catch (_: Exception) {
                        // Ignorar errores de pre-populado
                    }
                }

                instance
            }
        }
    }
}