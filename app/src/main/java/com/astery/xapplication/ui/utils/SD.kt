package com.astery.xapplication.ui.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat

/**
 * extensions for drawable
 * */
fun ImageView.setDrawable(drawable: Int, context:Context){
    this.setImageDrawable(
        ResourcesCompat.getDrawable(
            context.resources,
            drawable,
            context.theme
        )
    )
}

fun Context.getDraw(drawable: Int): Drawable?{
    return ResourcesCompat.getDrawable(
        this.resources,
        drawable,
        this.theme
    )
}