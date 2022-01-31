package com.astery.xapplication.ui.activity.popupDialogue

import android.content.Context
import android.view.LayoutInflater
import android.view.View

/** activity, that has dialogue panel must implement this interface*/
interface PanelHolder {
    fun findView(id: Int): View
    fun getContext(): Context
    fun getLayoutInflater():LayoutInflater
    /** get views that should be disabled when the panel opens */
    fun getBlockable():List<Blockable>
}

interface Blockable{
    fun setEnabled(enable:Boolean)
}

class BlockableView(val view:View):Blockable{
    override fun setEnabled(enable: Boolean) {
        view.isEnabled = enable
        //view.focusable = if (enable) View.FOCUSABLE else View.NOT_FOCUSABLE
        view.isClickable = enable
    }
}