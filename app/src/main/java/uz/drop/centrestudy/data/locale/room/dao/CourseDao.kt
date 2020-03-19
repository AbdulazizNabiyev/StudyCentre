package uz.drop.centrestudy.data.locale.room.dao

import androidx.room.*
import uz.drop.centrestudy.data.locale.room.entities.CourseData

@Dao
interface CourseDao {
    @Query("SELECT * FROM courses")
    fun getAll(): List<CourseData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(courseData: CourseData)

    @Delete
    fun delete(courseData: CourseData)

    @Update
    fun update(courseData: CourseData)

    @Query("SELECT * FROM courses c where c.user_id=:user_id")
    fun filterCoursesById(user_id:Long):List<CourseData>

    @Query("SELECT c.id, count(groups.id) group_count FROM courses c, groups where c.id=groups.course_id GROUP by c.id")
    fun getGroupsCount():List<GroupsCount>

    @Query("delete from courses where id=:id")
    fun deleteById(id:Long)
}

data class GroupsCount(var id:Long, var group_count:Int)
