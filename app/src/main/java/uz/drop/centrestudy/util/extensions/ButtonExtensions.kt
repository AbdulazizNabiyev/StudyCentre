package uz.drop.centrestudy.util.extensions

import android.os.Build
import android.widget.Button

fun Button.isEnabledCustomBackground(enabled:Boolean){
    if (enabled){
        isEnabled = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setTextColor(resources.getColor(android.R.color.white,context.theme))
        } else {
            setTextColor(resources.getColor(android.R.color.white))

        }
    }else {
        isEnabled = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setTextColor(resources.getColor(android.R.color.darker_gray, context.theme))
        } else {
            setTextColor(resources.getColor(android.R.color.darker_gray))

        }
    }
}