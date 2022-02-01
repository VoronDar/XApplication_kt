package com.astery.xapplication.ui.fragments.calendar

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.astery.xapplication.R
import com.astery.xapplication.model.entities.Event
import com.astery.xapplication.ui.adapterUtils.BaseAdapter
import com.astery.xapplication.ui.adapterUtils.BaseViewHolder
import com.astery.xapplication.ui.fragments.calendar.calendar_adapter.CalendarAdapter
import com.astery.xapplication.ui.utils.setDrawable
import java.util.*

class EventAdapter(units: ArrayList<Event?>?, context: Context) :
    BaseAdapter<EventAdapter.ViewHolder, Event?>(units, context) {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): BaseViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.unit_close_event, viewGroup, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(h: BaseViewHolder, position: Int) {
        val holder = h as ViewHolder
        val unit = units?.get(position)
        if (position == 0) return
        if (unit?.image == null){
            holder.image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.dating))
        } else{
            holder.image.setImageBitmap(unit.image)
        }
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(blockListener, itemView) {
        val image: ImageView = itemView.findViewById(R.id.image)
    }
}