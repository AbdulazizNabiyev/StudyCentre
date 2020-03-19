package uz.drop.centrestudy.util.extensions

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import uz.drop.centrestudy.data.locale.room.entities.CourseData
import uz.drop.centrestudy.data.locale.room.entities.GroupData
import uz.drop.centrestudy.data.locale.room.entities.StudentData
import uz.drop.centrestudy.data.locale.room.entities.UserData

fun String.toUsersListFromJson(): ArrayList<UserData> {
    val type = object : TypeToken<ArrayList<UserData>>() {}.type
    return Gson().fromJson<ArrayList<UserData>>(this, type)
}

fun String.toCoursesListFromJson(): ArrayList<CourseData> {
    val type = object : TypeToken<ArrayList<CourseData>>() {}.type
    return Gson().fromJson<ArrayList<CourseData>>(this, type)
}
fun String.toGroupsListFromJson(): ArrayList<GroupData> {
    val type = object : TypeToken<ArrayList<GroupData>>() {}.type
    return Gson().fromJson<ArrayList<GroupData>>(this, type)
}
fun String.toStudentsListFromJson(): ArrayList<StudentData> {
    val type = object : TypeToken<ArrayList<StudentData>>() {}.type
    return Gson().fromJson<ArrayList<StudentData>>(this, type)
}