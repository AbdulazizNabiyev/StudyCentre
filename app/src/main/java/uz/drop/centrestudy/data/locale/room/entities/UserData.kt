package uz.drop.centrestudy.data.locale.room.entities

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "users")
data class UserData(
    var username: String,
    var password: String,
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

) :
    Serializable
{
    @Ignore
    var lastUser: Boolean = false
}