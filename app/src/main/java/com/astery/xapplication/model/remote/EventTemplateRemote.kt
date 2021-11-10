package com.astery.xapplication.model.remote

import com.astery.xapplication.model.entities.EventTemplate
import java.util.*

/**
 * New events loaded from fb if their updateDate > DayOfLastUpdate
 * FB requires all fields to be in a class, so this class presents only due to it
 * */
data class EventTemplateRemote(var updateDate: Date):EventTemplate()