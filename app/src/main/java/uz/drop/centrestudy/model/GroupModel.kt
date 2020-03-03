package uz.drop.centrestudy.model

import java.io.Serializable

data class GroupModel(
    var imageUrl:String,
    var courseName: String,
    var name: String,
    var studentCount: Int = 0
) : Serializable