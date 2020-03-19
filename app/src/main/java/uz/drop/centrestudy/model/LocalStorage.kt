package uz.drop.centrestudy.model

import android.content.Context

class LocalStorage private constructor(context: Context) {

    companion object {
        lateinit var instance: LocalStorage
        fun init(context: Context) {
            instance =
                LocalStorage(context)

        }
    }

    private var pref = context.getSharedPreferences("data", Context.MODE_PRIVATE)

    var userData: String
        set(value) =
            pref.edit().putString("data", value).apply()
        get() = pref.getString("data", "")!!
    var lastUser: String
        set(value) =
            pref.edit().putString("lastUser", value).apply()
        get() = pref.getString("lastUser", "")!!
}