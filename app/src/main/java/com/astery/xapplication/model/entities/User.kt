package com.astery.xapplication.model.entities

/**
 * pojo class for a user
 */
data class User(var id: String, var gender:Int = 0,

    /** lists with id of articles  */
    var liked: List<String>? = null,
    var disliked: List<String>? = null,
    var watched: List<String>? = null,

    /** lists with id of advices  */
    var agreed: List<String>? = null,
    var disagreed: List<String>? = null
)