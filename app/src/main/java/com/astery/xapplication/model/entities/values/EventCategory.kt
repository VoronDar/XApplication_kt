package com.astery.xapplication.model.entities.values

import com.astery.xapplication.R

/**
 * type of event.
 * stores in eventTemplate
 * */
enum class EventCategory {
    /** happy, sad, lonely, etc */
    Mood{
        override fun getDrawableId(): Int {
            return R.drawable.dating
        }
    },
    /** pills, massages, etc */
    Medicine {
        override fun getDrawableId(): Int {
            return R.drawable.dating
        }
    },
    /** mainly pain */
    Feels {
        override fun getDrawableId(): Int {
            return R.drawable.dating
        }
    },
    MenstrualCycle {
        override fun getDrawableId(): Int {
            return R.drawable.dating
        }
    },
    /** unique events in you relations, sex */
    Dating {

        override fun getDrawableId(): Int {
            return R.drawable.dating
        }
    };

    abstract fun getDrawableId():Int
    companion object{
        /**
         * convert all values to arrayList
         * */
        fun toArray():ArrayList<EventCategory>{
            val array = ArrayList<EventCategory>()
            for (i in values()){
                array.add(i)
            }
            return array
        }
    }
}