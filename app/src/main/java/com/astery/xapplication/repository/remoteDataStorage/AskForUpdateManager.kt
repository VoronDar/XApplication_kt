package com.astery.xapplication.repository.remoteDataStorage

import com.astery.xapplication.model.entities.values.EventCategory
import com.astery.xapplication.repository.preferences.PreferenceEntity
import com.astery.xapplication.repository.preferences.Preferences
import java.util.*
import javax.inject.Inject

/**
 * check if
 * TODO(rename almost everything)
 * */
class AskForUpdateManager @Inject constructor(@set:Inject var preferences: Preferences) {
    fun isNeedToUpdate(entity: RemEntityUpdate): Boolean {
        val millisInDays = 86400000
        return preferences.getLong(entity.checkForUpdate) + UPDATE_INTERVAL_IN_DAYS * millisInDays <
                Calendar.getInstance().timeInMillis
    }

    fun getLastUpdated(entity: RemEntityUpdate?): Int {
        if (entity == null) return FIRST_UPDATE.toInt()
        return preferences.getLong(entity.lastUpdate).toInt()
    }

    fun setUpdated(entity: RemEntityUpdate, lastUpdated: Int) {
        preferences.set(entity.lastUpdate, lastUpdated.toLong())
        preferences.set(entity.checkForUpdate, JUST_UPDATED + Calendar.getInstance().timeInMillis)
    }


    companion object {
        const val UPDATE_INTERVAL_IN_DAYS: Long = 10
        const val JUST_UPDATED: Long = 0

        // TODO(rename) - это числовое значение для самого первого периода обновлений
        const val FIRST_UPDATE: Long = -1
    }
}

sealed class RemEntityUpdate(appendix: String) {
    val checkForUpdate: CheckForUpdatePreferenceEntity =
        CheckForUpdatePreferenceEntity(this::class.simpleName!! + appendix)
    val lastUpdate: LastUpdatePreferenceEntity =
        LastUpdatePreferenceEntity(this::class.simpleName!! + appendix)

    override fun toString(): String {
        return "RemEntityUpdate(${checkForUpdate.name})"
    }

}

class EvTemplateREU(category: EventCategory) : RemEntityUpdate(category.ordinal.toString())
class QuestionREU(templateId: Int) : RemEntityUpdate(templateId.toString())


class CheckForUpdatePreferenceEntity(name: String) : PreferenceEntity {
    override val name: String = name + "check"
    override val default: Long = AskForUpdateManager.UPDATE_INTERVAL_IN_DAYS
}

class LastUpdatePreferenceEntity(name: String) : PreferenceEntity {
    override val name: String = name + "last"
    override val default: Long = AskForUpdateManager.FIRST_UPDATE
}