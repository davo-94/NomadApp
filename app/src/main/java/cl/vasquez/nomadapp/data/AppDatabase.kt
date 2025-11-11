package cl.vasquez.nomadapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Post::class], version =1 )
abstract class AppDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao

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
                ).build()
                INSTANCE = instance
                instance

            }
        }
    }
}