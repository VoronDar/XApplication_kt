package com.astery.xapplication.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.astery.xapplication.R

/*

class TipsAdapter(units: List<AdvicesUnit>?, context: Fragment) :
    RecyclerView.Adapter<TipsAdapter.ViewHolder?>() {
    private var units: List<AdvicesUnit>?
    private var blockListener: BlockListener? = null
    private val context: Fragment
    fun setUnits(units: List<AdvicesUnit>?) {
        this.units = units
        notifyDataSetChanged()
    }

    fun setBlockListener(block_listener: BlockListener?) {
        blockListener = block_listener
    }

    interface BlockListener {
        fun onClick(position: Int)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.unit_tip, viewGroup, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val unit: AdvicesUnit = units!![position]
        holder.questionText.setText(unit.getQuestion())
        holder.answer.setText(unit.getAnswer())
        holder.questionNumber.setText(position + 1 + "")
        for (advice in unit.getAdvices()) {
            val view: View = context.layoutInflater.inflate(R.layout.unit_advice, null)
            (view.findViewById<View>(R.id.advice_text) as TextView).setText(advice.getText())
            view.findViewById<View>(R.id.dislikes_card).setVisibility(VS.Companion.get(false))
            view.findViewById<View>(R.id.likes_card).setVisibility(VS.Companion.get(false))
            holder.tipLayout.addView(view)
        }
    }

    val itemCount: Int
        get() = if (units == null) 0 else units!!.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val questionNumber: TextView
        val questionText: TextView
        val answer: TextView
        val tipLayout: LinearLayout

        init {
            questionNumber = itemView.findViewById<TextView>(R.id.question_number)
            questionText = itemView.findViewById<TextView>(R.id.question)
            answer = itemView.findViewById<TextView>(R.id.answer)
            tipLayout = itemView.findViewById<LinearLayout>(R.id.tip_layout)
            itemView.setOnClickListener { v: View? ->
                if (blockListener != null) {
                    blockListener!!.onClick(getAdapterPosition())
                }
            }
        }
    }

    init {
        this.units = units
        this.context = context
    }
}

 */