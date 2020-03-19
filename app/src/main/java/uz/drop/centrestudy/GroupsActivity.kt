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
import uz.drop.centrestudy.data.locale.room.AppDatabase
import uz.drop.centrestudy.data.locale.room.entities.GroupData
import uz.drop.centrestudy.model.LocalStorage
import uz.drop.centrestudy.util.extensions.toGroupsListFromJson

class GroupsActivity : AppCompatActivity() {
    private val groupsList = ArrayList<GroupData>()
    private val adapter = GroupAdapter(groupsList)
    private val groupsDao = AppDatabase.getDatabase().groupDao()
    private val studentsDao = AppDatabase.getDatabase().studentDao()
    val GROUP_REQUEST_CODE = 2
    val GROUP_EDIT_REQUEST_CODE = 20
    var courseId: Long=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_groups)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Groups"

        courseId = intent.getLongExtra("course_id",0)
        listView.adapter = adapter
        if (groupsDao.getAll().isNotEmpty()) {
            val list = groupsDao.getAll()
            val filter = groupsDao.filterGroupsById(courseId)
            groupsList.addAll(filter)
        }

        listView.setOnItemClickListener { parent, view, position, id ->
            startActivity(
                Intent(this, StudentActivity::class.java).putExtra(
                    "group_id",
                    groupsList[position].id
                )
            )
        }
        adapter.setOnDeleteClickListener { pos ->
            val removedGroup = groupsList.removeAt(pos)
            if (studentsDao.getAll().isNotEmpty()) {
                studentsDao.deleteStudentsById(removedGroup.id)
            }
            groupsDao.deleteById(removedGroup.id)
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
        groupsList.clear()
        val filter = groupsDao.filterGroupsById(courseId)
        groupsList.addAll(filter)
        if (groupsDao.getAll().isNotEmpty()) {
            groupsList.forEach {group->
                val studentsCount = groupsDao.getStudentsCount()
                studentsCount.forEach {
                    if (group.id==it.id){
                        group.studentCount=it.student_count
                        groupsDao.update(group)
                    }
                }
            }

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
                    ).putExtra("course_id", courseId), GROUP_REQUEST_CODE
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
            if (groupsDao.getAll().isNotEmpty()){
                groupsList.clear()
                val filter = groupsDao.filterGroupsById(courseId)
                groupsList.addAll(filter)

            }

            adapter.notifyDataSetChanged()
        }
    }
}
