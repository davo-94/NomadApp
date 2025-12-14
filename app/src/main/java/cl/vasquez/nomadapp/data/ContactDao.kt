package cl.vasquez.nomadapp.data

import androidx.room.*

@Dao
interface ContactDao {

    /**
     * Inserta un contacto en la tabla. Si ya existe un contacto con el mismo id, lo reemplaza.
     * En suspend porque las consultas a la bd se deben ejecutar fuera del hilo principal.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contact: Contact)

    //Obtener todos los contactos
    @Query("SELECT * FROM contact_table ORDER BY id DESC")
    suspend fun getAll(): List<Contact>

    //Eliminar contacto
    @Delete
    suspend fun delete(contact: Contact)
}
