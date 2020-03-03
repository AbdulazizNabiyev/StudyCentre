package uz.drop.centrestudy.model

import java.io.Serializable

data class CourseModel(
    var imageUrl:String,
    val username: String,
    var name: String,
    var groupCount: Int = 0
) : Serializable