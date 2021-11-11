package com.astery.xapplication.ui.fragments.transitionHelpers

sealed class NavigationTransition {}

class SharedAxisTransition:NavigationTransition() {
    var axis = 0
        private set
    fun setAxis(axis: Int): SharedAxisTransition {
        this.axis = axis
        return this
    }
}
