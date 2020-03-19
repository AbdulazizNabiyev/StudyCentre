package uz.drop.centrestudy.data.locale.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "students")
data class StudentData(

    @ColumnInfo(name="group_id")
    var groupId: Long,
    var name: String,
    var surname: String,
    @PrimaryKey(autoGenerate = true)
    var id :Long=0
) : Serializable