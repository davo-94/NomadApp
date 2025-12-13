package cl.vasquez.nomadapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * DAO para gestionar usuarios admin en la BD local
 */
@Dao
interface AdminUserDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<AdminUser>)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: AdminUser)
    
    @Update
    suspend fun updateUser(user: AdminUser)
    
    @Delete
    suspend fun deleteUser(user: AdminUser)
    
    @Query("SELECT * FROM admin_users ORDER BY username")
    fun getAllUsers(): Flow<List<AdminUser>>
    
    @Query("SELECT * FROM admin_users WHERE id = :userId")
    suspend fun getUserById(userId: Long): AdminUser?
    
    @Query("SELECT * FROM admin_users WHERE syncPending = 1")
    suspend fun getPendingChanges(): List<AdminUser>
    
    @Query("DELETE FROM admin_users")
    suspend fun clearAll()
}
