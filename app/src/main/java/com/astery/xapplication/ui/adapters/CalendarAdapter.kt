package com.astery.xapplication.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.astery.xapplication.R
import com.astery.xapplication.ui.adapters.units.DayUnit
import com.google.android.material.card.MaterialCardView
import timber.log.Timber
import java.util.ArrayList

class CalendarAdapter(blocks: ArrayList<DayUnit>, context: Context) :
    RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {
    var units: ArrayList<DayUnit> = blocks
    private var blockListener: BlockListener? = null
    private var selectedDay: Int
    private var context: Context
    fun setBlockListener(block_listener: BlockListener) {
        blockListener = block_listener
    }

    fun setSelectedDay(day: Int) {
        selectedDay = day
        notifyDataSetChanged()
    }

    interface BlockListener {
        fun onClick(position: Int)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.getContext())
            .inflate(R.layout.unit_calendar, viewGroup, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val unit: DayUnit = units[position]
        holder.day.text = unit.day.toString()
        holder.card.isGone = !(unit.enabled)
        if (unit.day == selectedDay) {
            Timber.i("${holder.card} and $context")
            holder.card.setBackgroundColor(context.resources.getColor(R.color.selected_card_color))
            holder.card.strokeColor = context.resources.getColor(R.color.black)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val day: TextView = itemView.findViewById(R.id.day)
        val card: MaterialCardView = itemView.findViewById(R.id.card)

        init {
            itemView.setOnClickListener {
                if (blockListener != null) {
                    blockListener!!.onClick(adapterPosition)
                }
            }
        }
    }

    init {
        selectedDay = 0
        this.context = context
    }

    override fun getItemCount(): Int {
        return units.size
    }
}