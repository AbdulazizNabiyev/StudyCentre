package uz.drop.centrestudy.data.locale.room.dao

import androidx.room.*
import uz.drop.centrestudy.data.locale.room.entities.StudentData

@Dao
interface StudentDao {
    @Query("SELECT * FROM students")
    fun getAll(): List<StudentData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(studentData: StudentData)

    @Delete
    fun delete(studentData: StudentData)

    @Update
    fun update(studentData: StudentData)

    @Query("delete from students where group_id=:group_id")
    fun deleteStudentsById(group_id:Long)

    @Query("SELECT * FROM students s where s.group_id=:group_id")
    fun filterStudentsById(group_id:Long):List<StudentData>
    /*@Insert
    fun insertAll(studentData: List<StudentData>)*/

/*
@Query("SELECT name FROM students WHERE id in (:id1)")
    fun getAges(id1: List<Int>): List<Int>*/
}
