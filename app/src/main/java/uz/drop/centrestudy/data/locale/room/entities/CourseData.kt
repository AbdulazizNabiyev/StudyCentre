package uz.drop.centrestudy.data.locale.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "courses")
data class CourseData(
    @ColumnInfo(name = "user_id")
    val userId: Long,
    var name: String,
    var imageUrl: String,
    var groupCount:Int=0,
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
) : Serializable