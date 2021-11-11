package com.astery.xapplication.ui.adapters.utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.astery.xapplication.ui.adapters.BlockListener

open class BaseViewHolder(blockListener: BlockListener?, itemView: View) : RecyclerView.ViewHolder(itemView) {
    init {
        itemView.setOnClickListener {
            blockListener?.onClick(adapterPosition)
        }
    }
}