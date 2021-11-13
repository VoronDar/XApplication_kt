package com.astery.xapplication.model.entities.converters

/*
class WarningConditionConverter {
    @TypeConverter
    fun toDb(value: WarningTemplate.WarningCondition?): String? {
        return if (value == null) null else Gson().toJson(value)
    }

    @TypeConverter
    fun toClass(data: String?): WarningTemplate.WarningCondition? {
        return if (data == null) null else try {
            val obj = JSONObject(data)
            Gson().fromJson(obj.toString(), WarningTemplate.WarningCondition::class.java)
        } catch (e: JSONException) {
            e.printStackTrace()
            throw RuntimeException("WarningCondition converting error " + e.message)
        }
    }
}

 */