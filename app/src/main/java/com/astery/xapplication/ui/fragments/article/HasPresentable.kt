package com.astery.xapplication.ui.fragments.article

import androidx.lifecycle.LiveData

interface HasPresentable{
    val element: LiveData<Presentable>
}