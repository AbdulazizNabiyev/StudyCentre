package uz.drop.centrestudy

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_courses.*
import kotlinx.android.synthetic.main.item.view.*
import uz.drop.centrestudy.adapters.CourseAdapter
import uz.drop.centrestudy.data.locale.room.AppDatabase
import uz.drop.centrestudy.data.locale.room.entities.CourseData
import uz.drop.centrestudy.model.LocalStorage
import uz.drop.centrestudy.data.locale.room.entities.UserData
import uz.drop.centrestudy.util.extensions.toCoursesListFromJson

class CoursesActivity : AppCompatActivity() {
    private val localStorage = LocalStorage.instance
    private val courseDao = AppDatabase.getDatabase().courseDao()
    private val groupsDao = AppDatabase.getDatabase().groupDao()
    private val studentsDao = AppDatabase.getDatabase().studentDao()
    private val courseList = ArrayList<CourseData>()
    private val courseAdapter = CourseAdapter(courseList)
    private val gson = Gson()
    private val COURSE_REQUEST_CODE = 1
    private val COURSE_EDIT_REQUEST_CODE = 10
    private lateinit var user: UserData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_courses)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Courses"
        val userFromIntent = intent.getSerializableExtra("currentUser") as? UserData
        user = userFromIntent ?: gson.fromJson(localStorage.lastUser, UserData::class.java)

        val courses = courseDao.getAll()

        listView.adapter = courseAdapter

        listView.setOnItemClickListener { _, view, position, _ ->
            startActivity(
                Intent(this, GroupsActivity::class.java).putExtra(
                    "course_id",
                    courseList[position].id
                )
            )

        }
        courseAdapter.setOnEditClickListener { pos ->
            startActivityForResult(
                Intent(this, AddingActivity::class.java).putExtra(
                    "actionName",
                    "editCourse"
                ).putExtra("editableCourse", courseList[pos]),
                COURSE_EDIT_REQUEST_CODE
            )
        }
        courseAdapter.setOnDeleteClickListener { pos ->
            val removedCourse = courseList.removeAt(pos)
            if (groupsDao.getAll().isNotEmpty()) {
                val groupsList = groupsDao.getAll()
                groupsList.forEach { group ->
                    if (group.courseId == removedCourse.id) {
                        studentsDao.deleteStudentsById(group.id)
                    }
                }
                groupsDao.deleteGroupsById(removedCourse.id)
            }
            courseDao.deleteById(removedCourse.id)

            courseAdapter.notifyDataSetChanged()
        }
    }

    override fun onResume() {
        super.onResume()
        val filter = courseDao.filterCoursesById(user.id)
        courseList.clear()
        courseList.addAll(filter)
        if (courseDao.getAll().isNotEmpty()) {
            courseList.forEach {course->
                val groupsCount = courseDao.getGroupsCount()
                groupsCount.forEach {
                    if (course.id==it.id){
                        course.groupCount=it.group_count
                        courseDao.update(course)
                    }
                }
            }
            courseAdapter.notifyDataSetChanged()

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.my_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.addMenu -> {
                startActivityForResult(
                    Intent(this, AddingActivity::class.java).putExtra(
                        "actionName",
                        "course"
                    ).putExtra("USER", user), COURSE_REQUEST_CODE
                )
            }
            R.id.logOut -> {
                startActivity(Intent(this, SignInActivity::class.java))
                localStorage.lastUser = ""
                finish()
            }
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == COURSE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            courseList.clear()
            val filter = courseDao.filterCoursesById(user.id)
            courseList.addAll(filter)
            courseAdapter.notifyDataSetChanged()
        }
    }
}
