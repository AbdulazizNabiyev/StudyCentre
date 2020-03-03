package uz.drop.centrestudy

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_sign_up.*
import uz.drop.centrestudy.model.LocalStorage
import uz.drop.centrestudy.model.UserModel
import uz.drop.centrestudy.util.extensions.isEnabledCustomBackground

class SignUpActivity : AppCompatActivity() {
    private val local = LocalStorage.instance
    private val gson = Gson()
    private val usersList = ArrayList<UserModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title="Sign Up"
        val type = object : TypeToken<ArrayList<UserModel>>() {}.type
        if (local.userData.isNotEmpty()) {
            val list: ArrayList<UserModel> =
                gson.fromJson<ArrayList<UserModel>>(local.userData, type)
            usersList.addAll(list)
        }
        username.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                usersList.forEach {
                    if (it.username == s.toString()) {
                        layoutUsername.error = "This username is already occupied"
                        signUpBtn.isEnabledCustomBackground(false)
                    } else {
                        layoutUsername.error = null
                        signUpBtn.isEnabledCustomBackground(true)
                    }
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })

        signUpBtn.setOnClickListener {
            if (password.text.toString() == passwordConfirm.text.toString()) {
                val userModel = UserModel(
                    username.text.toString(),
                    password.text.toString()
                )
                usersList.add(userModel)
                val s = gson.toJson(usersList)
                local.userData = s
                finish()
            } else {
                Toast.makeText(this, "Passwords must be the same", Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }
}
