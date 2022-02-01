package com.astery.xapplication.ui.loadingState

import android.content.res.Resources
import com.astery.xapplication.R

/** exception that may come with Result<>.failure from repository.
 * Required for LoadingStateError to describe the error for the user*/
interface LoadingErrorReason{
    fun getStringForUI(res: Resources):String
}
class InternetConnectionException:Throwable(), LoadingErrorReason{
    override fun getStringForUI(res: Resources): String {
        return res.getString(R.string.loading_state_error_internet)
    }
}
class UnexpectedBugException:Throwable(), LoadingErrorReason{
    override fun getStringForUI(res: Resources): String {
        return res.getString(R.string.loading_state_error_bug)
    }
}