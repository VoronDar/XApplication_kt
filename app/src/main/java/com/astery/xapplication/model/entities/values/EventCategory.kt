package com.astery.xapplication.model.entities.values

import com.astery.xapplication.R


enum class EventCategory {
    MOOD{
        override fun getDrawableId(): Int {
            return R.drawable.dating
        }
    }, MEDICINE {
        override fun getDrawableId(): Int {
            return R.drawable.dating
        }
    }, FEELS {
        override fun getDrawableId(): Int {
            return R.drawable.dating
        }
    }, CYCLE {
        override fun getDrawableId(): Int {
            return R.drawable.dating
        }
    }, SEX {

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
            for (i in EventCategory.values()){
                array.add(i)
            }
            return array
        }
    }
}