package uz.xsoft.lesson16pdp13.utils.extesions

import android.content.res.Resources

val Int.px: Int get() = (this / Resources.getSystem().displayMetrics.density).toInt()
val Int.dp: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()
