package com.astery.xapplication.ui.fragments.article

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.astery.xapplication.databinding.UnitArticlePageBinding
import com.astery.xapplication.model.entities.Event
import com.astery.xapplication.ui.adapterUtils.BaseAdapter
import com.astery.xapplication.ui.adapterUtils.BaseViewHolder

/**
 * adapter for pages (ArticleFragment)
 * */
class ArticleAdapter(pageCount: Int, context: Context) :
    BaseAdapter<ArticleAdapter.ViewHolder, Event?>(null, context) {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): BaseViewHolder {
        val binding =
            UnitArticlePageBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }


    /**
     * number of pages. Until items are loaded it equals 1
     * */
    var pageCount: Int = pageCount
        set(value) {
            for (i in (field + 1) until value) {
                notifyItemInserted(i)
            }
            field = value
        }

    var selectedPos: Int = 0
        private set(value) {
            val oldSelected = selectedPos
            field = value

            slideSelector(oldSelected)

            notifyItemChanged(oldSelected)
            notifyItemChanged(selectedPos)

            listener?.onClick(oldSelected, selectedPos)
        }

    var listener: BlockListener? = null
    private var recycler: RecyclerView? = null


    /**
     * change selected page (move back or forward)
     * */
    fun slidePage(forward: Boolean) {
        if (forward) {
            if (selectedPos + 1 == pageCount) return
            selectedPos += 1
        } else {
            if (selectedPos == 0) return
            selectedPos -= 1
        }
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(h: BaseViewHolder, position: Int) {
        val binding = (h as ViewHolder).binding
        binding.pos = position + 1
        binding.selected = (selectedPos == position)
    }

    override fun onViewDetachedFromWindow(h: BaseViewHolder) {
        super.onViewDetachedFromWindow(h)
        val binding = (h as ViewHolder).binding
        binding.unbind()
    }

    override fun getItemCount(): Int {
        return pageCount
    }


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recycler = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        recycler = null
    }

    /** scroll recycler view, so the selected page always on screen*/
    private fun slideSelector(oldSelected:Int){
        val buffer = 3
        val position:Int =
            if (oldSelected < selectedPos) {
                if ((pageCount - 1) - selectedPos > buffer) selectedPos + buffer
                else pageCount-1
            } else {
                if ((selectedPos + 1) > buffer) selectedPos - buffer
                else 0
            }
        recycler?.scrollToPosition(position)
    }

    inner class ViewHolder(val binding: UnitArticlePageBinding) :
        BaseViewHolder(blockListener, binding.root) {
        init {
            binding.root.setOnClickListener {
                selectedPos = adapterPosition
            }
        }
    }

    interface BlockListener {
        fun onClick(oldPos: Int, newPos: Int)
    }
}