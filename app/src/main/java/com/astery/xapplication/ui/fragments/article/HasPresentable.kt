package com.astery.xapplication.ui.fragments.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * presentable is an article or an item
 * They have really similar items, so it's there
 * */
interface HasPresentable{
    val element: LiveData<Result<Presentable>>
    val presentable: LiveData<Presentable>
}