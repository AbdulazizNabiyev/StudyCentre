package uz.drop.centrestudy.model

import java.io.Serializable

data class UserModel(val username: String, val password: String, val lastUser: Boolean = false) :
    Serializable