package uz.drop.centrestudy

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_sign_in.*
import uz.drop.centrestudy.data.locale.room.AppDatabase
import uz.drop.centrestudy.model.LocalStorage
import uz.drop.centrestudy.data.locale.room.entities.UserData
import uz.drop.centrestudy.util.extensions.toUsersListFromJson

class SignInActivity : AppCompatActivity() {
    private val gson = Gson()
    val usersDao = AppDatabase.getDatabase().usersDao()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        val localStorage = LocalStorage.instance
        if (localStorage.lastUser.isNotEmpty()) {
            startActivity(Intent(this, CoursesActivity::class.java))
            finish()
        }

        signUpBtn.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        termsBtn.setOnClickListener {
            startActivity(Intent(this, TermsActivity::class.java))
        }
        forgetPassword.setOnClickListener {

        }
        signInBtn.setOnClickListener {
            val username = username.text.toString()
            val password = password.text.toString()
            val notRememberedUser =
                UserData(
                    username,
                    password
                )
            val usersList = usersDao.getAll()
            if (usersList.isNotEmpty()) {
                usersList.forEach {
                    if (username == it.username && password == it.password) {
                        if (rememberCheckBox.isChecked) {
                            it.lastUser = true
                            notRememberedUser.lastUser=true
                            notRememberedUser.id = it.id
                            localStorage.lastUser = gson.toJson(notRememberedUser)
                            startActivity(Intent(this, CoursesActivity::class.java)
                                .putExtra("currentUser", notRememberedUser)
                            )
                            finish()
                            return@setOnClickListener
                        } else {
                            notRememberedUser.id = it.id
                            startActivity(
                                Intent(
                                    this,
                                    CoursesActivity::class.java
                                ).putExtra("currentUser", notRememberedUser)
                            )
                            finish()
                            return@setOnClickListener
                        }

                    }


                }
                Toast.makeText(
                    this,
                    "Username or password is incorrect",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(this, "Please sign up first", Toast.LENGTH_SHORT).show()
            }

        }

    }
}
