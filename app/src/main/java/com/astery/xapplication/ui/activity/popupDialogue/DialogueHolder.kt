package com.astery.xapplication.ui.activity.popupDialogue

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding

/** declares what should be shown in panel, declare listeners and etc*/
interface DialogueHolder {
    /** get fully set binding. When you implement this func you must cal onClose in some listener*/
    fun getBinding(inflater: LayoutInflater, container: ViewGroup?, onClose:()->Unit):ViewDataBinding
    fun enable(binding:ViewDataBinding, enable:Boolean)
    fun doOnClose()
}