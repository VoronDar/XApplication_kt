package com.astery.xapplication.ui.fragments.calendar.calendar_adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import com.astery.xapplication.R
import com.astery.xapplication.ui.activity.popupDialogue.Blockable
import com.astery.xapplication.ui.adapterUtils.BaseAdapter
import com.astery.xapplication.ui.adapterUtils.BaseViewHolder
import com.google.android.material.card.MaterialCardView
import java.util.*

/**
 * days of week
 * */
class CalendarAdapter(units: ArrayList<DayUnit>?, context: Context) :
    BaseAdapter<CalendarAdapter.ViewHolder, DayUnit>(units, context ), Blockable {
    var selectedDay: Int = 0
    set(value) {
        val oldValue = selectedDay
        field = value
        notifyItemChanged(units!!.indexOfFirst { it.day == oldValue })
        notifyItemChanged(units!!.indexOfFirst { it.day == value })
    }
    var isEnable:Boolean = true

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.unit_calendar, viewGroup, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(h: BaseViewHolder, position: Int) {
        val holder = h as ViewHolder


        val unit: DayUnit = units!![position]

        holder.day.text = unit.day.toString()
        holder.card.isGone = !(unit.enabled)
        if (unit.day == selectedDay) {
            holder.card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.neutralSurfaceColor))
            holder.card.strokeColor = ContextCompat.getColor(context, R.color.black)
            holder.day.setTextColor(ContextCompat.getColor(context, R.color.black))
        } else{
            holder.card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.appBar))
            holder.card.strokeColor = ContextCompat.getColor(context, R.color.divider_color)
            holder.day.setTextColor(ContextCompat.getColor(context, R.color.onBackground))
        }
    }


    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return units?.size?: 0
    }

    override fun setEnabled(enable: Boolean) {
        isEnable = enable
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(blockListener, itemView) {
        val day: TextView = itemView.findViewById(R.id.day)
        val card: MaterialCardView = itemView.findViewById(R.id.card)
    }
}