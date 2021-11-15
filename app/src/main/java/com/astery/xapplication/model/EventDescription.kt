package com.astery.xapplication.model

/**
 * list of details for an event.
 * How did the user answer questions
 *
 * Maybe later this class will be contains some more fields that just map
 * */
class EventDescription(
    /** advice questionId - answerId  */
    var properties: Map<Int, Int>
)