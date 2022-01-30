package com.astery.xapplication.ui.fragments.articlesList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.astery.xapplication.databinding.UnitArticleBinding
import com.astery.xapplication.model.entities.Article
import com.astery.xapplication.model.entities.FeedBackState
import com.astery.xapplication.repository.feetback.OnFeedbackListener
import com.astery.xapplication.ui.activity.popupDialogue.Blockable
import com.astery.xapplication.ui.adapterUtils.BlockListener
import com.astery.xapplication.ui.pageFeetback.FeedBackStorage
import timber.log.Timber

class ArticlesListAdapter(val viewModel:ArticlesListViewModel, val recyclerView: RecyclerView) : PagingDataAdapter<Article, ArticlesListAdapter.ViewHolder>(ArticleDiffUtils()), Blockable {
    private var isBlocked = false
    override fun setEnabled(enable: Boolean) {
        isBlocked = !enable
    }
    /** important: listen for id, not for position */
    var blockListener:BlockListener? = null

    var triedToLoadImage = ArrayList<Int>()


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // TODO(null pointer exception)
        // TODO(на рендеринге нулевого изображения случается failed to render an image)
        if (getItem(position) != null) {
            holder.bind(getItem(position)!!)


            Timber.d("pos - $position, item - ${getItem(position)} ")

            if (!triedToLoadImage.contains(position)){
                triedToLoadImage.add(position)
                viewModel.getImage(getItem(position)!!, position, this)
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(UnitArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    inner class ViewHolder(private val binding: UnitArticleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article) {
            binding.article = article
            binding.feedBackStorage = FeedBackStorage(article.likes, article.dislikes)

            binding.root.setOnClickListener {
                if (isBlocked) return@setOnClickListener
                blockListener?.onClick(article)
            }

            if (article.image != null)
                binding.itemImage.setImageBitmap(article.image!!)
        }
    }



    class ArticleDiffUtils : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean = oldItem == newItem
    }

    interface BlockListener{
        fun onClick(article:Article)
    }
}