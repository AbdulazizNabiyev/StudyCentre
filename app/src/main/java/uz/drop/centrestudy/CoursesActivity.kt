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
import uz.drop.centrestudy.model.CourseModel
import uz.drop.centrestudy.model.LocalStorage
import uz.drop.centrestudy.model.UserModel
import uz.drop.centrestudy.util.extensions.toCoursesListFromJson
import uz.drop.centrestudy.util.extensions.toGroupsListFromJson
import uz.drop.centrestudy.util.extensions.toStudentsListFromJson

class CoursesActivity : AppCompatActivity() {
    private val localStorage = LocalStorage.instance
    private val courseList = ArrayList<CourseModel>()
    private val courseAdapter = CourseAdapter(courseList)
    private val gson = Gson()
    private val COURSE_REQUEST_CODE = 1
    private val COURSE_EDIT_REQUEST_CODE = 10
    private lateinit var user: UserModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_courses)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Courses"
        val userFromIntent = intent.getSerializableExtra("currentUser") as? UserModel
        user = userFromIntent ?: gson.fromJson(localStorage.lastUser, UserModel::class.java)
        listView.adapter = courseAdapter
        if (localStorage.courses.isNotEmpty()) {
            val list = localStorage.courses.toCoursesListFromJson()
            val filter = list.filter {
                it.username == user.username
            }
            courseList.addAll(filter)
        }

        listView.setOnItemClickListener { _, view, _, _ ->
            startActivity(
                Intent(this, GroupsActivity::class.java).putExtra(
                    "courseName",
                    view.nameText.text.toString()
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
            if (localStorage.groups.isNotEmpty()){
                val groupsList = localStorage.groups.toGroupsListFromJson()
                groupsList.forEach { group ->
                    if (group.courseName == removedCourse.name) {
                        groupsList.remove(group)
                        if (localStorage.students.isNotEmpty()){
                            val students = localStorage.students.toStudentsListFromJson()
                            students.forEach { student ->
                                if (student.groupName == group.name) {
                                    students.remove(student)
                                }
                            }
                            localStorage.students = gson.toJson(students)
                        }

                    }
                }
                localStorage.groups = gson.toJson(groupsList)

            }



            localStorage.courses = gson.toJson(courseList)

            courseAdapter.notifyDataSetChanged()
        }
    }

    override fun onResume() {
        super.onResume()
        if (localStorage.courses.isNotEmpty()) {
            val coursesList = localStorage.courses.toCoursesListFromJson()
            courseList.clear()
            val filter = coursesList.filter {
                it.username == user.username
            }
            courseList.addAll(filter)
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
            val coursesJson = data!!.getStringExtra("coursesJson")!!
            val coursesList = coursesJson.toCoursesListFromJson()
            courseList.clear()
            val filter = coursesList.filter {
                it.username == user.username
            }
            courseList.addAll(filter)
            courseAdapter.notifyDataSetChanged()
        }
    }
}
