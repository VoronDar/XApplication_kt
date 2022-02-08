package com.astery.xapplication.ui.activity.interfaces

import com.astery.xapplication.ui.activity.popupDialogue.Blockable
import com.astery.xapplication.ui.activity.popupDialogue.DialogueHolder

interface PanelUsable {
    fun getBlockable():List<Blockable>
    fun getDialogueHolder(): DialogueHolder
}