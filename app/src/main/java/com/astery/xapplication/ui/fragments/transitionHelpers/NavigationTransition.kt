package com.astery.xapplication.ui.fragments.transitionHelpers

sealed class NavigationTransition {
    /** first = true will be applied for current fragment  */
    var isFirst = true
        protected set

    fun setFirst(isFirst: Boolean): NavigationTransition {
        this.isFirst = isFirst
        return this
    }

    abstract fun reverseCopy(): NavigationTransition
}

class SharedAxisTransition:NavigationTransition() {
    var axis = 0
        private set

    /** firstParent = true for cases when current fragment - parent  */
    var firstParent = false
        private set

    fun setAxis(axis: Int): SharedAxisTransition {
        this.axis = axis
        return this
    }

    fun setFirstParent(isParent: Boolean): SharedAxisTransition {
        firstParent = isParent
        return this
    }

    override fun reverseCopy(): NavigationTransition {
        return SharedAxisTransition().setAxis(axis)
    }
}
