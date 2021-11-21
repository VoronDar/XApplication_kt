package com.astery.xapplication.ui.fragments.calendar

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.astery.xapplication.R
import com.astery.xapplication.databinding.AlertDeleteCardBinding

/** alert dialog that ask 'are you sure' after pressing the 'delete item' button */
class DeleteEventDialogue(layoutInflater: LayoutInflater, context: Context) {

    val binding = AlertDeleteCardBinding.inflate(layoutInflater, null, false)
    private val adb: AlertDialog.Builder = AlertDialog.Builder(context, R.style.CustomAlertDialog)
    private val alertDialog: AlertDialog

    init {
        adb.setView(binding.root)
        alertDialog = adb.create()
        binding.cancel.setOnClickListener { alertDialog.cancel() }
    }

    fun show() {
        alertDialog.show()
    }

    fun setOnOkListener(listener: () -> Unit):DeleteEventDialogue {
        binding.submit.setOnClickListener {
            listener()
            alertDialog.cancel()
        }
        return this
    }
}