package uz.drop.centrestudy

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_student.*
import uz.drop.centrestudy.adapters.StudentAdapter
import uz.drop.centrestudy.data.locale.room.AppDatabase
import uz.drop.centrestudy.model.LocalStorage
import uz.drop.centrestudy.data.locale.room.entities.StudentData
import uz.drop.centrestudy.util.extensions.toStudentsListFromJson

class StudentActivity : AppCompatActivity() {
    private val localStorage = LocalStorage.instance
    private val studentsList = ArrayList<StudentData>()
    private val adapter = StudentAdapter(studentsList)
    private val gson = Gson()
    private val studentsDao = AppDatabase.getDatabase().studentDao()
    val STUDENT_REQUEST_CODE = 3
    val STUDENT_EDIT_REQUEST_CODE = 30
     var groupId: Long=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title="Students"

        groupId = intent.getLongExtra("group_id",0) as Long
        listView.adapter = adapter
        if (studentsDao.getAll().isNotEmpty()) {
            val list = studentsDao.getAll()
            val filter = studentsDao.filterStudentsById(groupId)
            studentsList.addAll(filter)
        }

        adapter.setOnDeleteClickListener {pos->
            studentsDao.delete(studentsList[pos])
            studentsList.removeAt(pos)
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
        if (studentsDao.getAll().isNotEmpty()) {
            studentsList.clear()
            val filter=studentsDao.filterStudentsById(groupId)
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
                    ).putExtra("group_id", groupId), STUDENT_REQUEST_CODE
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
            studentsList.clear()

        }
    }
}
