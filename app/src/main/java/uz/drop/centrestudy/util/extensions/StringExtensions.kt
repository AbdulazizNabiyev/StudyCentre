package uz.drop.centrestudy.util.extensions

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import uz.drop.centrestudy.model.CourseModel
import uz.drop.centrestudy.model.GroupModel
import uz.drop.centrestudy.model.StudentModel
import uz.drop.centrestudy.model.UserModel

fun String.toUsersListFromJson(): ArrayList<UserModel> {
    val type = object : TypeToken<ArrayList<UserModel>>() {}.type
    return Gson().fromJson<ArrayList<UserModel>>(this, type)
}

fun String.toCoursesListFromJson(): ArrayList<CourseModel> {
    val type = object : TypeToken<ArrayList<CourseModel>>() {}.type
    return Gson().fromJson<ArrayList<CourseModel>>(this, type)
}
fun String.toGroupsListFromJson(): ArrayList<GroupModel> {
    val type = object : TypeToken<ArrayList<GroupModel>>() {}.type
    return Gson().fromJson<ArrayList<GroupModel>>(this, type)
}
fun String.toStudentsListFromJson(): ArrayList<StudentModel> {
    val type = object : TypeToken<ArrayList<StudentModel>>() {}.type
    return Gson().fromJson<ArrayList<StudentModel>>(this, type)
}