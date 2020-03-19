package uz.drop.centrestudy.data.locale.room.dao

import androidx.room.*
import uz.drop.centrestudy.data.locale.room.entities.GroupData

@Dao
interface GroupsDao {
    @Query("SELECT * FROM groups")
    fun getAll(): List<GroupData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(groupData: GroupData)

    @Delete
    fun delete(groupData: GroupData)

    @Update
    fun update(groupData: GroupData)

    @Query("delete from groups where course_id=:course_id")
    fun deleteGroupsById(course_id: Long)

    @Query("delete from groups where id=:id")
    fun deleteById(id: Long)

    @Query("SELECT * FROM groups g where g.course_id=:course_id")
    fun filterGroupsById(course_id: Long): List<GroupData>

    @Query("SELECT g.id, count(students.id) student_count FROM groups g, students where g.id=students.group_id GROUP by g.id")
    fun getStudentsCount(): List<StudentsCount>
}

data class StudentsCount(var id: Long, var student_count: Int)
