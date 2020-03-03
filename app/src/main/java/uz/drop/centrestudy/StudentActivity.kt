package uz.drop.centrestudy

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_student.*
import uz.drop.centrestudy.adapters.StudentAdapter
import uz.drop.centrestudy.model.LocalStorage
import uz.drop.centrestudy.model.StudentModel
import uz.drop.centrestudy.util.extensions.toCoursesListFromJson
import uz.drop.centrestudy.util.extensions.toGroupsListFromJson
import uz.drop.centrestudy.util.extensions.toStudentsListFromJson

class StudentActivity : AppCompatActivity() {
    private val localStorage = LocalStorage.instance
    private val studentsList = ArrayList<StudentModel>()
    private val adapter = StudentAdapter(studentsList)
    private val gson = Gson()
    val STUDENT_REQUEST_CODE = 3
    val STUDENT_EDIT_REQUEST_CODE = 30
    lateinit var groupName: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title="Students"

        groupName = intent.getStringExtra("groupName") as String
        listView.adapter = adapter
        if (localStorage.students.isNotEmpty()) {
            val list = localStorage.students.toStudentsListFromJson()
            val filter = list.filter {
                it.groupName == groupName
            }
            studentsList.addAll(filter)
        }

        adapter.setOnDeleteClickListener {pos->
            studentsList.removeAt(pos)
            localStorage.students = gson.toJson(studentsList)
            val oldList = localStorage.groups.toGroupsListFromJson()
            oldList.forEach {
                if (it.name == groupName) {
                    it.studentCount--
                }
            }
            localStorage.groups = gson.toJson(oldList)
            adapter.notifyDataSetChanged()

        }
        adapter.setOnEditClickListener {pos->
            startActivityForResult(
                Intent(this, AddingActivity::class.java).putExtra(
                    "actionName",
                    "editStudent"
                ).putExtra("editableStudent", studentsList[pos]),
                STUDENT_EDIT_REQUEST_CODE
            )
        }
    }

    override fun onResume() {
        super.onResume()
        if (localStorage.students.isNotEmpty()) {
            val list = localStorage.students.toStudentsListFromJson()
            studentsList.clear()
            val filter = list.filter {
                it.groupName == groupName
            }
            studentsList.addAll(filter)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.my_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.setGroupVisible(R.id.courseMenu, false)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.addMenu -> {
                startActivityForResult(
                    Intent(this, AddingActivity::class.java).putExtra(
                        "actionName",
                        "student"
                    ).putExtra("GROUPNAME", groupName), STUDENT_REQUEST_CODE
                )
            }

            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == STUDENT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val studentsJson = data!!.getStringExtra("studentsJson")!!
            val allList = studentsJson.toStudentsListFromJson()
            studentsList.clear()
            val filter = allList.filter {
                it.groupName == groupName
            }
            studentsList.addAll(filter)
            adapter.notifyDataSetChanged()
            val oldList = localStorage.groups.toGroupsListFromJson()
            oldList.forEach {
                if (it.name == groupName) {
                    it.studentCount++
                }
            }
            localStorage.groups = gson.toJson(oldList)
        }
    }
}
