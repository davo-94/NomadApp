package cl.vasquez.nomadapp.data

import androidx.room.*

@Dao
interface ContactDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contact: Contact)

    @Query("SELECT * FROM contact_table ORDER BY id DESC")
    suspend fun getAll(): List<Contact>

    @Delete
    suspend fun delete(contact: Contact)
}
