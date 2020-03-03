package uz.drop.centrestudy.App

import android.app.Application
import android.util.Log
import uz.drop.centrestudy.model.LocalStorage

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        LocalStorage.init(this)
    }

}