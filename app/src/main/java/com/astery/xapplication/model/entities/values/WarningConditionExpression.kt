package com.astery.xapplication.model.entities.values

/*
enum class WarningConditionExpression {
    AT_LEAST {
        // alert if at least <time> times in a <period>
        override fun check(time: Int): Boolean {
            return allEvents!!.size >= time
        }
    },
    NOT_MANY {
        // alert if lesser then <time> times in a <period>,
        override fun check(time: Int): Boolean {
            return allEvents!!.size < time
        }
    },
    AT_LEAST_PERCENT {
        // alert if at least <time>% times in a <period>
        override fun check(time: Int): Boolean {
            return allEvents!!.size < time
        }
    },
    NOT_MANY_PERCENT {
        // alert if lesser then <time>% times in a <period>
        override fun check(time: Int): Boolean {
            return allEvents!!.size < time
        }
    },
    AT_LEAST_PROPERTY_VALUES {
        // alert if property <propertyKey> value in <value> more then <time>% times in all events.
        override fun check(time: Int): Boolean {
            var count = 0
            for ((_, _, eventDescription) in allEvents!!) {
                val value: String = eventDescription.properties.get(propertyKey)
                if (values!!.contains(value)) count += 1
            }
            return if (count == 0) false else 100 * (allEvents!!.size / count) >= time
        }
    },
    NOT_MANY_PROPERTY_VALUES {
        // alert if property <propertyKey> value in <value> less then <time>% times in all events.
        override fun check(time: Int): Boolean {
            var count = 0
            for ((_, _, eventDescription) in allEvents!!) {
                val value: String = eventDescription.properties.get(propertyKey)
                if (values!!.contains(value)) count += 1
            }
            return if (count == 0) false else 100 * (allEvents!!.size / count) < time
        }
    };

    var propertyKey: String? = null
    var values: List<String>? = null
    var allEvents: List<Event>? = null
    abstract fun check(time: Int): Boolean
}

 */