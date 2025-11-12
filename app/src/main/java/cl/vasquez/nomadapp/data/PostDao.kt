package cl.vasquez.nomadapp.data

import androidx.room.*

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: Post)

    @Query("SELECT * FROM posts ORDER BY id DESC")
    suspend fun getAll(): List<Post>

    @Delete
    suspend fun delete(post: Post)
}