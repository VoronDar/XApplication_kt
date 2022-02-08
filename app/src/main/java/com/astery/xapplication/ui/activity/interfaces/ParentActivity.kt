package com.astery.xapplication.ui.activity.interfaces

import com.astery.xapplication.model.entities.ArticleTag
import com.astery.xapplication.ui.fragments.calendar.CalendarFragment

/**
 * interface to link MainActivity with its fragments
 * */
interface ParentActivity {
    fun changeTitle(title:String?)
    fun showCalendarNav(show:Boolean, listener: CalendarFragment.CalendarMonthNavListener)
    fun showSearchBar(show:Boolean, fragment: SearchUsable)
    fun showFilters(show:Boolean, fragment: FiltersUsable)
    fun updateFilters(list:List<ArticleTag>)
    /** hide keyboard */
    fun hideSearchKeyboard()
    fun showPanel(caller:PanelUsable)
}
