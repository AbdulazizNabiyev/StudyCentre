package uz.drop.centrestudy.model

import java.io.Serializable

data class StudentModel(
    var groupName: String,
    var name: String,
    var surname: String
) : Serializable