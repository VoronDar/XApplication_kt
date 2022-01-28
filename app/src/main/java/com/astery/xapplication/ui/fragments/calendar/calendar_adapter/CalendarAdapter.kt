package com.astery.xapplication.ui.fragments.calendar.calendar_adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isGone
import com.astery.xapplication.R
import com.astery.xapplication.ui.adapterUtils.BaseAdapter
import com.astery.xapplication.ui.adapterUtils.BaseViewHolder
import com.google.android.material.card.MaterialCardView
import timber.log.Timber
import java.util.*

/**
 * days of week
 * */
class CalendarAdapter(units: ArrayList<DayUnit>?, context: Context) :
    BaseAdapter<CalendarAdapter.ViewHolder, DayUnit>(units, context ) {
    var selectedDay: Int = 0
    set(value) {
        val oldValue = selectedDay
        field = value
        Timber.d("$oldValue, $value")
        notifyItemChanged(units!!.indexOfFirst { it.day == oldValue })
        notifyItemChanged(units!!.indexOfFirst { it.day == value })
    }

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
            Timber.i("${holder.card} and $context")
            holder.card.setBackgroundColor(context.resources.getColor(R.color.selected_card_color))
            holder.card.strokeColor = context.resources.getColor(R.color.black)
        } else{
            holder.card.setBackgroundColor(context.resources.getColor(R.color.white))
            holder.card.strokeColor = context.resources.getColor(R.color.calendar_unit_stroke_color)
        }
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(blockListener, itemView) {
        val day: TextView = itemView.findViewById(R.id.day)
        val card: MaterialCardView = itemView.findViewById(R.id.card)
    }
}