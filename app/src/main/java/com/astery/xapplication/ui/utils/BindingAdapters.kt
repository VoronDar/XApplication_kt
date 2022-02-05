package com.astery.xapplication.ui.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.util.*

object BindingAdapters{
    @BindingAdapter("app:capitalizedText")
    @JvmStatic fun setCapitalizedText(view: TextView?, string: String?) {
        view?.text = string?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }
}