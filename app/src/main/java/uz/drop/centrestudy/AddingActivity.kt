package uz.drop.centrestudy

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_adding.*
import uz.drop.centrestudy.model.*
import uz.drop.centrestudy.util.extensions.toCoursesListFromJson
import uz.drop.centrestudy.util.extensions.toGroupsListFromJson
import uz.drop.centrestudy.util.extensions.toStudentsListFromJson

class AddingActivity : AppCompatActivity() {
    private val local = LocalStorage.instance
    private val gson = Gson()
    private val courseList = ArrayList<CourseModel>()
    private val groupList = ArrayList<GroupModel>()
    private val studentList = ArrayList<StudentModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adding)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        when (intent.getStringExtra("actionName")) {
            "course" -> {
                title="Adding Course"
                if (local.courses.isNotEmpty()) {
                    courseList.addAll(local.courses.toCoursesListFromJson())
                }
                saveButton.setOnClickListener {
                    val newName = nameAdd.text.toString()
                    val userFromIntent = intent.getSerializableExtra("USER") as? UserModel
                    val user =
                        userFromIntent ?: gson.fromJson(local.lastUser, UserModel::class.java)
                    if (newName.isEmpty() || imageUrlAdd.text.toString().isEmpty()) {
                        Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    } else {
                        val courseModel = CourseModel(
                            imageUrlAdd.text.toString(),
                            user.username,
                            newName
                        )
                        courseList.add(courseModel)
                        val listJson = gson.toJson(courseList)
                        local.courses = listJson
                        val intentResult = Intent()
                        intentResult.putExtra("coursesJson", listJson)
                        setResult(Activity.RESULT_OK, intentResult)
                        finish()
                    }

                }
            }
            "group" -> {
                title="Adding Group"
                if (local.groups.isNotEmpty()) {
                    groupList.addAll(local.groups.toGroupsListFromJson())
                }
                saveButton.setOnClickListener {
                    val newName = nameAdd.text.toString()

                    if (newName.isEmpty() || imageUrlAdd.text.toString().isEmpty()) {
                        Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    } else {
                        val groupModel = GroupModel(
                            imageUrlAdd.text.toString(),
                            intent.getStringExtra("COURSENAME")!!,
                            newName
                        )
                        groupList.add(groupModel)
                        val listJson = gson.toJson(groupList)
                        local.groups = listJson
                        val intentResult = Intent()
                        intentResult.putExtra("groupsJson", listJson)
                        setResult(Activity.RESULT_OK, intentResult)
                        finish()
                    }

                }
            }
            "student" -> {
                title="Adding Student"
                if (local.students.isNotEmpty()) {
                    studentList.addAll(local.students.toStudentsListFromJson())
                }
                layoutImage.hint = "Surname"
                saveButton.setOnClickListener {
                    val newName = nameAdd.text.toString()

                    if (newName.isEmpty() || imageUrlAdd.text.toString().isEmpty()) {
                        Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    } else {
                        val studentModel = StudentModel(
                            intent.getStringExtra("GROUPNAME")!!,
                            newName,
                            imageUrlAdd.text.toString()
                        )
                        studentList.add(studentModel)
                        val listJson = gson.toJson(studentList)
                        local.students = listJson
                        val intentResult = Intent()
                        intentResult.putExtra("studentsJson", listJson)
                        setResult(Activity.RESULT_OK, intentResult)
                        finish()
                    }

                }
            }
            "editCourse" -> {
                val courseEdit = intent.getSerializableExtra("editableCourse") as CourseModel
                title = "Editing Course"
                nameAdd.setText(courseEdit.name)
                imageUrlAdd.setText(courseEdit.imageUrl)
                saveButton.setOnClickListener {
                    val newName = nameAdd.text.toString()
                    if (newName.isEmpty() || imageUrlAdd.text.toString().isEmpty()) {
                        Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    } else {
                        val editedList = local.courses.toCoursesListFromJson()
                        editedList.forEach {
                            if (it.name == courseEdit.name && it.username == courseEdit.username) {
                                it.name = newName
                                it.imageUrl = imageUrlAdd.text.toString()
                            }
                        }
                        local.courses = gson.toJson(editedList)
                        val editedGroupList = local.groups.toGroupsListFromJson()
                        editedGroupList.forEach {
                            if (it.courseName == courseEdit.name) {
                                it.courseName = newName
                            }
                        }
                        local.groups = gson.toJson(editedGroupList)
                        finish()
                    }

                }
            }
            "editGroup" -> {
                val groupEdit = intent.getSerializableExtra("editableGroup") as GroupModel
                title = "Editing Group"
                nameAdd.setText(groupEdit.name)
                imageUrlAdd.setText(groupEdit.imageUrl)
                saveButton.setOnClickListener {
                    val newName = nameAdd.text.toString()
                    if (newName.isEmpty() || imageUrlAdd.text.toString().isEmpty()) {
                        Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    } else {
                        val editedList = local.groups.toGroupsListFromJson()
                        editedList.forEach {
                            if (it.name == groupEdit.name && it.courseName == groupEdit.courseName) {
                                it.name = newName
                                it.imageUrl = imageUrlAdd.text.toString()
                            }
                        }
                        local.groups = gson.toJson(editedList)
                        val editedStudentList = local.students.toStudentsListFromJson()
                        editedStudentList.forEach {
                            if (it.groupName == groupEdit.name) {
                                it.groupName = newName
                            }
                        }
                        local.students = gson.toJson(editedStudentList)
                        finish()
                    }

                }
            }
            "editStudent" -> {
                val studentEdit = intent.getSerializableExtra("editableStudent") as StudentModel
                title = "Editing Student"
                layoutImage.hint = "Surname"
                nameAdd.setText(studentEdit.name)
                imageUrlAdd.setText(studentEdit.surname)
                saveButton.setOnClickListener {
                    val newName = nameAdd.text.toString()
                    val newSurname = imageUrlAdd.text.toString()
                    if (newName.isEmpty() || newSurname.isEmpty()) {
                        Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    } else {
                        val editedList = local.students.toStudentsListFromJson()
                        editedList.forEach {
                            if (it.name == studentEdit.name && it.groupName == studentEdit.groupName) {
                                it.name = newName
                                it.surname = newSurname
                            }
                        }
                        local.students = gson.toJson(editedList)
                        finish()
                    }

                }
            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }
}
