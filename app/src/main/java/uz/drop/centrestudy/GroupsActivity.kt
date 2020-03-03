package uz.drop.centrestudy

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_groups.*
import kotlinx.android.synthetic.main.item.view.*
import uz.drop.centrestudy.adapters.GroupAdapter
import uz.drop.centrestudy.model.GroupModel
import uz.drop.centrestudy.model.LocalStorage
import uz.drop.centrestudy.util.extensions.toCoursesListFromJson
import uz.drop.centrestudy.util.extensions.toGroupsListFromJson
import uz.drop.centrestudy.util.extensions.toStudentsListFromJson

class GroupsActivity : AppCompatActivity() {
    private val localStorage = LocalStorage.instance
    private val groupsList = ArrayList<GroupModel>()
    private val adapter = GroupAdapter(groupsList)
    private val gson = Gson()
    val GROUP_REQUEST_CODE = 2
    val GROUP_EDIT_REQUEST_CODE = 20
    lateinit var courseName: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_groups)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Groups"

        courseName = intent.getStringExtra("courseName") as String
        listView.adapter = adapter
        if (localStorage.groups.isNotEmpty()) {
            val list = localStorage.groups.toGroupsListFromJson()
            val filter = list.filter {
                it.courseName == courseName
            }
            groupsList.addAll(filter)
        }

        listView.setOnItemClickListener { parent, view, position, id ->
            startActivity(
                Intent(this, StudentActivity::class.java).putExtra(
                    "groupName",
                    view.nameText.text.toString()
                )
            )
        }
        adapter.setOnDeleteClickListener { pos ->
            val removedGroup = groupsList.removeAt(pos)
            val students = localStorage.students.toStudentsListFromJson()
            students.forEach { student ->
                if (student.groupName == removedGroup.name) {
                    students.remove(student)

                }
            }

            val oldList = localStorage.courses.toCoursesListFromJson()
            oldList.forEach {
                if (it.name == courseName) {
                    it.groupCount--
                }
            }
            localStorage.courses = gson.toJson(oldList)

            localStorage.groups = gson.toJson(groupsList)
            localStorage.students = gson.toJson(students)
            adapter.notifyDataSetChanged()

        }
        adapter.setOnEditClickListener { pos ->
            startActivityForResult(
                Intent(this, AddingActivity::class.java).putExtra(
                    "actionName",
                    "editGroup"
                ).putExtra("editableGroup", groupsList[pos]),
                GROUP_EDIT_REQUEST_CODE
            )
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


    override fun onResume() {
        super.onResume()
        if (localStorage.groups.isNotEmpty()) {
            val list = localStorage.groups.toGroupsListFromJson()
            groupsList.clear()
            val filter = list.filter {
                it.courseName == courseName
            }
            groupsList.addAll(filter)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.addMenu -> {
                startActivityForResult(
                    Intent(this, AddingActivity::class.java).putExtra(
                        "actionName",
                        "group"
                    ).putExtra("COURSENAME", courseName), GROUP_REQUEST_CODE
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
        if (requestCode == GROUP_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val groupsJson = data!!.getStringExtra("groupsJson")!!
            val coursesList = groupsJson.toGroupsListFromJson()
            groupsList.clear()
            val filter = coursesList.filter {
                it.courseName == courseName
            }
            groupsList.addAll(filter)
            adapter.notifyDataSetChanged()
            val oldList = localStorage.courses.toCoursesListFromJson()
            oldList.forEach {
                if (it.name == courseName) {
                    it.groupCount++
                }
            }
            localStorage.courses = gson.toJson(oldList)
        }
    }
}
