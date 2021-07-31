package tech.intouch.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import tech.intouch.models.User

@Dao
interface AccountDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Insert
    fun insertAll(vararg users: User)
}
