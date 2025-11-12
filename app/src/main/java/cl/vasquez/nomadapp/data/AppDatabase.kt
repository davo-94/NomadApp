package cl.vasquez.nomadapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.migration.Migration

// Version = 4 fuerza la reconstrucción de tablas o migración
// Se incluyen todas las entidades: Post, Contact y User
@Database(entities = [Post::class, Contact::class, User::class], version = 4, exportSchema = true)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun postDao(): PostDao
    abstract fun contactDao(): ContactDao
    abstract fun userDao(): UserDao

    companion object {
        /**
         * Patrón singleton asegura que la base de datos se cree una sola
         * vez y esté accesible desde toda la app.
         */
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Migración desde versión 3 → 4.
         * Agrega la nueva columna imageUris (TEXT) en la tabla de publicaciones.
         * Si existía imageUri, se migra su valor al nuevo campo.
         * (Desactivada temporalmente mientras se usa fallbackToDestructiveMigration)
         */
        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Agrega columna imageUris si no existe
                db.execSQL("ALTER TABLE posts ADD COLUMN imageUris TEXT NOT NULL DEFAULT ''")

                // Copia valor de la columna anterior si existía
                db.execSQL(
                    """
                    UPDATE posts
                    SET imageUris = CASE
                        WHEN imageUri IS NOT NULL AND imageUri <> '' THEN imageUri
                        ELSE ''
                    END
                    """.trimIndent()
                )
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "nomadapp_database"
                )
                    /**
                     * fallbackToDestructiveMigration() elimina la base existente
                     * si hay un cambio de versión y la reconstruye desde cero.
                     *
                     * Esta opción es ideal durante desarrollo, ya que evita
                     * conflictos de migraciones al modificar entidades.
                     *
                     * En producción, se debería reemplazar por addMigrations(...)
                     * para preservar los datos del usuario.
                     */
                    .fallbackToDestructiveMigration()
                    //.addMigrations(MIGRATION_3_4) // ← activa esto más adelante si necesitas migrar sin borrar datos
                    .build()

                INSTANCE = instance

                /**
                 * Pre-poblar usuarios de prueba en la base de datos.
                 * Este bloque se ejecuta al crear la instancia y agrega
                 * dos usuarios predeterminados (solo si no existen).
                 *
                 * Se ejecuta en un hilo separado (Dispatchers.IO) para no bloquear la UI.
                 */
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val dao = instance.userDao()

                        // Usuario administrador
                        if (dao.getByEmail("admin@nomadapp.com") == null) {
                            dao.insert(
                                User(
                                    email = "admin@nomadapp.com",
                                    password = "abc1234",
                                    role = "admin"
                                )
                            )
                        }

                        // Usuario invitado
                        if (dao.getByEmail("user@nomadapp.com") == null) {
                            dao.insert(
                                User(
                                    email = "user@nomadapp.com",
                                    password = "password123",
                                    role = "guest"
                                )
                            )
                        }
                    } catch (_: Exception) {
                        // Ignorar errores de pre-populado (por ejemplo, si la DB aún se inicializa)
                    }
                }

                instance
            }
        }
    }
}
