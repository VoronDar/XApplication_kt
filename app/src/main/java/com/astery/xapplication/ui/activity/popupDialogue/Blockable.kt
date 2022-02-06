package com.astery.xapplication.ui.activity.popupDialogue

import android.view.View


interface Blockable{
    fun setEnabled(enable:Boolean)
}

class BlockableView(val view: View):Blockable{
    override fun setEnabled(enable: Boolean) {
        view.isEnabled = enable
        view.isClickable = enable
    }
}