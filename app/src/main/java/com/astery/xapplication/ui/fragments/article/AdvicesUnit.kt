package com.astery.xapplication.ui.fragments.article

import com.astery.xapplication.model.entities.Advice

/** unit for TipsAdapter */
data class AdvicesUnit(val question: String, val answer: String, val advice: List<Advice>,
                       val itemId: String
)