package cl.vasquez.nomadapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cl.vasquez.nomadapp.data.UserDao


//Version = 2 fuerza la reconstrucción de tablas
@Database(entities = [Post::class, Contact::class, User::class], version = 2 )
abstract class AppDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun contactDao(): ContactDao
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
                instance

            }
        }
    }
}