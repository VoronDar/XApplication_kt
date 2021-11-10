package com.astery.xapplication.ui.adapters.units

import com.astery.xapplication.model.entities.Advise

/** unit for TipsAdapter */
data class AdvicesUnit(val question: String, val answer: String, val advices: List<Advise>,
                  val itemId: String
)