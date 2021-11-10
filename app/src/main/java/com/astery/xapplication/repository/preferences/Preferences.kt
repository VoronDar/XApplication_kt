package com.astery.xapplication.repository.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/** Transporter between sharedPreferences and others
 * */
class Preferences @Inject constructor(@ApplicationContext private var ctx:Context) {

    private fun getPref(): SharedPreferences {
        return ctx.getSharedPreferences("prefs", 0)
    }

    fun getLong(pref:PreferenceEntity): Long {
        return (getPref().getLong(
            pref.name,
            pref.default
        ))
    }

    fun set(pref: PreferenceEntity, value: Long) {
        getPref().edit { putLong(pref.name, value) }
    }
}