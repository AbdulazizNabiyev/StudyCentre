package uz.xsoft.lesson16pdp13.utils.extesions

import android.os.Build
import android.widget.ImageView
import androidx.annotation.ColorRes
import com.squareup.picasso.Picasso
import uz.drop.centrestudy.R

fun ImageView.setTint(@ColorRes colorRes: Int) {
    val color = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        resources.getColor(colorRes, context.theme)
    } else {
        resources.getColor(colorRes)
    }
    setColorFilter(color)
}

fun ImageView.loadFromUrl(url: String) {
    Picasso.get()
        .load(url)
        .resize(80.dp, 80.dp)
        .placeholder(R.drawable.avatar)
        .centerCrop()
        .into(this)
}