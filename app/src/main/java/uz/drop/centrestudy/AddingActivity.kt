package uz.drop.centrestudy

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_adding.*
import uz.drop.centrestudy.data.locale.room.AppDatabase
import uz.drop.centrestudy.data.locale.room.entities.CourseData
import uz.drop.centrestudy.data.locale.room.entities.GroupData
import uz.drop.centrestudy.data.locale.room.entities.StudentData
import uz.drop.centrestudy.data.locale.room.entities.UserData
import uz.drop.centrestudy.model.*
import uz.drop.centrestudy.util.extensions.isEnabledCustomBackground

class AddingActivity : AppCompatActivity() {
    private val courseDao = AppDatabase.getDatabase().courseDao()
    private val groupsDao = AppDatabase.getDatabase().groupDao()
    private val studentsDao = AppDatabase.getDatabase().studentDao()
    val gson = Gson()
    val local = LocalStorage.instance
    private val courseList = ArrayList<CourseData>()
    private val groupList = ArrayList<GroupData>()
    private val studentList = ArrayList<StudentData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adding)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        when (intent.getStringExtra("actionName")) {
            "course" -> {
                title = "Adding Course"
                if (courseDao.getAll().isNotEmpty()) {
                    courseList.addAll(courseDao.getAll())
                    nameAdd.addTextChangedListener(object : TextWatcher {
                        override fun afterTextChanged(s: Editable?) {
                            courseList.forEach {
                                if (it.name == s.toString()) {
                                    layoutName.error = "This course has already occupied"
                                    saveButton.isEnabledCustomBackground(false)
                                } else {
                                    layoutName.error = null
                                    saveButton.isEnabledCustomBackground(true)
                                }
                            }
                        }

                        override fun beforeTextChanged(
                            s: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int
                        ) {
                        }

                        override fun onTextChanged(
                            s: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int
                        ) {
                        }

                    })
                }


                saveButton.setOnClickListener {
                    val newName = nameAdd.text.toString()
                    val user = intent.getSerializableExtra("USER") as? UserData
                    if (newName.isEmpty() || imageUrlAdd.text.toString().isEmpty()) {
                        Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    } else {
                        val courseModel =
                            CourseData(
                                user!!.id,
                                newName,
                                imageUrlAdd.text.toString()
                            )
                        courseDao.insert(courseModel)
                        courseList.add(courseModel)
                        setResult(Activity.RESULT_OK)
                        finish()
                    }

                }
            }
            "group" -> {
                title = "Adding Group"
                if (groupsDao.getAll().isNotEmpty()) {
                    groupList.addAll(groupsDao.getAll())
                }
                saveButton.setOnClickListener {
                    val newName = nameAdd.text.toString()
                    if (newName.isEmpty() || imageUrlAdd.text.toString().isEmpty()) {
                        Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    } else {
                        val groupModel =
                            GroupData(
                                intent.getLongExtra("course_id", 0),
                                newName,
                                imageUrlAdd.text.toString()
                            )
                        groupList.add(groupModel)
                        groupsDao.insert(groupModel)
                        setResult(Activity.RESULT_OK)
                        finish()
                    }

                }
            }
            "student" -> {
                title = "Adding Student"
                if (studentsDao.getAll().isNotEmpty()) {
                    studentList.addAll(studentsDao.getAll())
                }
                layoutImage.hint = "Surname"
                saveButton.setOnClickListener {
                    val newName = nameAdd.text.toString()

                    if (newName.isEmpty() || imageUrlAdd.text.toString().isEmpty()) {
                        Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    } else {
                        val studentModel =
                            StudentData(
                                intent.getLongExtra("group_id",0),
                                newName,
                                imageUrlAdd.text.toString()
                            )
                        studentList.add(studentModel)
                        studentsDao.insert(studentModel)
                        setResult(Activity.RESULT_OK)
                        finish()
                    }

                }
            }
            "editCourse" -> {
                val courseEdit = intent.getSerializableExtra("editableCourse") as CourseData
                title = "Editing Course"
                nameAdd.setText(courseEdit.name)
                imageUrlAdd.setText(courseEdit.imageUrl)
                saveButton.setOnClickListener {
                    val newName = nameAdd.text.toString()
                    if (newName.isEmpty() || imageUrlAdd.text.toString().isEmpty()) {
                        Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    } else {
                        courseEdit.name=newName
                        courseEdit.imageUrl=imageUrlAdd.text.toString()
                        courseDao.update(courseEdit)
                        finish()
                    }

                }
            }
            "editGroup" -> {
                val groupEdit = intent.getSerializableExtra("editableGroup") as GroupData
                title = "Editing Group"
                nameAdd.setText(groupEdit.name)
                imageUrlAdd.setText(groupEdit.imageUrl)
                saveButton.setOnClickListener {
                    val newName = nameAdd.text.toString()
                    if (newName.isEmpty() || imageUrlAdd.text.toString().isEmpty()) {
                        Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    } else {
                        groupEdit.name=newName
                        groupEdit.imageUrl=imageUrlAdd.text.toString()
                        groupsDao.update(groupEdit)
                        finish()
                    }

                }
            }
            "editStudent" -> {
                val studentEdit = intent.getSerializableExtra("editableStudent") as StudentData
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
                        studentEdit.name=newName
                        studentEdit.surname=newSurname
                        studentsDao.update(studentEdit)
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
