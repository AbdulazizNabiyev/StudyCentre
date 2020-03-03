package uz.drop.centrestudy

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_sign_in.*
import uz.drop.centrestudy.model.LocalStorage
import uz.drop.centrestudy.model.UserModel
import uz.drop.centrestudy.util.extensions.toUsersListFromJson

class SignInActivity : AppCompatActivity() {
    private val gson = Gson()
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
            val notRememberedUser = UserModel(username, password)
            if (localStorage.userData.isNotEmpty()) {
                val usersList = localStorage.userData.toUsersListFromJson()
                usersList.forEach {
                    if (username == it.username && password == it.password) {
                        if (rememberCheckBox.isChecked) {
                            localStorage.lastUser = gson.toJson(UserModel(username, password, true))
                            startActivity(Intent(this, CoursesActivity::class.java))
                            finish()
                            return@setOnClickListener
                        } else {
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
