package com.astery.xapplication.ui.adapters.utils

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.astery.xapplication.ui.adapters.BlockListener
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*


abstract class BaseAdapter<T, R> (units: List<R>?, var context:Context):
    RecyclerView.Adapter<BaseViewHolder>() {
    var units:List<R>? = null
    set(value){
        field = value
        notifyDataSetChanged()
    }

    var blockListener: BlockListener? = null

    init{
        this.units = units
    }


    override fun getItemCount(): Int {
        if (units == null) return 0
        return units!!.size
    }


}
