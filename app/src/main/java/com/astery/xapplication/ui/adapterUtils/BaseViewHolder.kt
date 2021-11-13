package com.astery.xapplication.ui.adapterUtils

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * abstraction for all RecyclerView.viewHolders
 * */
open class BaseViewHolder(blockListener: BlockListener?, itemView: View) : RecyclerView.ViewHolder(itemView) {
    init {
        itemView.setOnClickListener {
            blockListener?.onClick(adapterPosition)
        }
    }
}