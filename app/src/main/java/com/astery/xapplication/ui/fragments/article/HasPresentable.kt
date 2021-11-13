package com.astery.xapplication.ui.fragments.article

import androidx.lifecycle.LiveData

/**
 * presentable is an article or an item
 * They have really similar items, so it's there
 * */
interface HasPresentable{
    val element: LiveData<Presentable>
}