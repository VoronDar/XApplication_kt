package com.astery.xapplication.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import com.astery.xapplication.R
import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.ImageView
import com.astery.xapplication.model.entities.Event
import java.util.ArrayList

class EventAdapter(private var units: ArrayList<Event?>?, private val context: Context) :
    RecyclerView.Adapter<EventAdapter.ViewHolder>() {
    private var blockListener: BlockListener? = null
    fun setUnits(units: ArrayList<Event?>?) {
        this.units = units
        notifyDataSetChanged()
    }

    fun setBlockListener(block_listener: BlockListener) {
        blockListener = block_listener
    }

    interface BlockListener {
        fun onClick(position: Int)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.unit_close_event, viewGroup, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //val unit = units?.get(position)
        //SD.Companion.setDrawable(holder.image, R.drawable.dating, context)
        // TODO - set custom images
    }

    override fun getItemCount(): Int {
        return if (units == null) 0 else units!!.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.image)

        init {
            itemView.setOnClickListener {
                if (blockListener != null) {
                    blockListener!!.onClick(adapterPosition)
                }
            }
        }
    }
}