package uz.drop.centrestudy.data.locale.room.dao

import androidx.room.*
import uz.drop.centrestudy.data.locale.room.entities.UserData

@Dao
interface UsersDao {
    @Query("SELECT * FROM users")
    fun getAll(): List<UserData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(userData: UserData):Long

    @Delete
    fun delete(userData: UserData)

    @Update
    fun update(userData: UserData)

    @Query("Select id from users where username=:username and password=:password")
    fun getIdOfUser(username:String, password:String):Long


}
