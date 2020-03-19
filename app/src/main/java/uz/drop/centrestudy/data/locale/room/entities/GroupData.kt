package uz.drop.centrestudy.data.locale.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "groups")
data class GroupData(
    @ColumnInfo(name="course_id")
    var courseId: Long,
    var name: String,
    var imageUrl:String,
    var studentCount:Int=0,
    @PrimaryKey(autoGenerate = true)
    var id :Long=0
) : Serializable