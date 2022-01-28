package com.astery.xapplication.ui.activity.interfaces

import com.astery.xapplication.ui.fragments.calendar.CalendarFragment

/**
 * interface to link MainActivity with its fragments
 * */
interface ParentActivity {
    fun changeTitle(title:String?)
    fun showMenuNav(show:Boolean, listener: CalendarFragment.MenuNavListener)
    fun showSearchBar(show:Boolean, fragment: SearchUsable)
}
