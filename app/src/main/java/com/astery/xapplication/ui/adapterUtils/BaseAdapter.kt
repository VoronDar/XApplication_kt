package com.astery.xapplication.ui.adapterUtils

import android.content.Context
import androidx.recyclerview.widget.RecyclerView

/**
 * abstraction for all adapters
 * */
abstract class BaseAdapter<T, R>(units: List<R>?, var context: Context) :
    RecyclerView.Adapter<BaseViewHolder>() {
    var units: List<R>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    open var blockListener: BlockListener? = null

    init {
        this.units = units
    }


    override fun getItemCount(): Int {
        if (units == null) return 0
        return units!!.size
    }


}
