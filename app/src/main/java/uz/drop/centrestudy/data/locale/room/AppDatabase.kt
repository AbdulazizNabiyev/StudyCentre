package uz.drop.centrestudy.data.locale.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uz.drop.centrestudy.data.locale.room.dao.CourseDao
import uz.drop.centrestudy.data.locale.room.dao.GroupsDao
import uz.drop.centrestudy.data.locale.room.dao.StudentDao
import uz.drop.centrestudy.data.locale.room.dao.UsersDao
import uz.drop.centrestudy.data.locale.room.entities.CourseData
import uz.drop.centrestudy.data.locale.room.entities.GroupData
import uz.drop.centrestudy.data.locale.room.entities.StudentData
import uz.drop.centrestudy.data.locale.room.entities.UserData

@Database(
    entities = [UserData::class, CourseData::class, GroupData::class, StudentData::class],
    version = 2, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun studentDao(): StudentDao
    abstract fun courseDao(): CourseDao
    abstract fun groupDao(): GroupsDao
    abstract fun usersDao(): UsersDao

    companion object {
        private var instance: AppDatabase? = null

        fun init(context: Context) {
            instance = Room.databaseBuilder(context, AppDatabase::class.java, "study_centre_data")
                .allowMainThreadQueries()
                .build()
        }

        fun getDatabase(): AppDatabase {
            return instance!!
        }
    }
}