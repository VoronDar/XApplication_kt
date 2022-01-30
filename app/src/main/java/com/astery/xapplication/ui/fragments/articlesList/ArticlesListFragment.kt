package com.astery.xapplication.ui.fragments.articlesList

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.insertHeaderItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.astery.xapplication.databinding.DialogFilterBinding
import com.astery.xapplication.databinding.FragmentCategoryBinding
import com.astery.xapplication.model.entities.Article
import com.astery.xapplication.model.entities.ArticleTag
import com.astery.xapplication.model.entities.ArticleTagType
import com.astery.xapplication.ui.activity.interfaces.FiltersUsable
import com.astery.xapplication.ui.activity.interfaces.SearchUsable
import com.astery.xapplication.ui.activity.popupDialogue.Blockable
import com.astery.xapplication.ui.activity.popupDialogue.DialogueHolder
import com.astery.xapplication.ui.fragments.XFragment
import com.astery.xapplication.ui.fragments.addEvent.customizeEvent.AddEventFragmentDirections
import com.astery.xapplication.ui.fragments.article.ArticleFragment
import com.astery.xapplication.ui.fragments.transitionHelpers.SharedAxisTransition
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import timber.log.Timber


/**
 * menu -> select article
 * */
@AndroidEntryPoint
class ArticlesListFragment : XFragment(), SearchUsable, FiltersUsable {
    private val binding: FragmentCategoryBinding
        get() = bind as FragmentCategoryBinding

    val viewModel: ArticlesListViewModel by viewModels()
    private var articleListAdapter: ArticlesListAdapter? = null

    var keywords: String = ""
    var tags: MutableList<ArticleTag> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTransition(SharedAxisTransition().setAxis(MaterialSharedAxis.Z))
    }

    override fun onStart() {
        super.onStart()
        parentActivity.showSearchBar(true, this)
        parentActivity.showFilters(true, this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bind = FragmentCategoryBinding.inflate(inflater, container, false)
        //binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return bind.root
    }

    override fun prepareAdapters() {
        articleListAdapter =
            ArticlesListAdapter(viewModel, binding.recyclerView)
        binding.recyclerView.adapter = articleListAdapter
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        articleListAdapter!!.blockListener = object: ArticlesListAdapter.BlockListener {
            override fun onClick(article:Article) {
                moveToArticle(article)
            }
        }
    }



    override fun getFragmentTitle(): String? {
        return null
    }

    override fun setViewModelListeners() {
        prepareAdapters()
        requestArticleFlow()
    }

    override fun getSearchText(value: String) {
        keywords = value
        requestArticleFlow()
    }

    override fun setFilters(value: MutableList<ArticleTag>) {
        tags = value
        requestArticleFlow()
    }

    private fun requestArticleFlow() {
        lifecycleScope.launch {
            viewModel.requestFlow(keywords, tags).collectLatest { source ->
                articleListAdapter?.triedToLoadImage?.clear()
                articleListAdapter?.submitData(source)
            }
        }
    }

    override fun getBlockable(): List<Blockable> {
        return listOf(articleListAdapter!!)
    }

    override fun getDialogueHolder(): DialogueHolder {
        val selectFilterTypeAdapter = SelectFilterTypeAdapter(null, requireContext())
        return object:DialogueHolder{

            override fun getBinding(
                inflater: LayoutInflater,
                container: ViewGroup?,
                onClose: () -> Unit
            ): ViewDataBinding {
                val binding =  DialogFilterBinding.inflate(inflater, container, false)
                binding.accept.setOnClickListener {
                    setFilters(mutableListOf())
                    onClose()
                }

                selectFilterTypeAdapter.selectedTags = tags
                selectFilterTypeAdapter.units = ArticleTagType.values().toList()
                binding.recyclerView.adapter = selectFilterTypeAdapter
                binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                binding.recyclerView.isNestedScrollingEnabled = false
                binding.recyclerView.overScrollMode = RecyclerView.OVER_SCROLL_NEVER

                return binding
            }

            override fun enable(binding: ViewDataBinding, enable: Boolean) {

            }

            override fun doOnClose() {
                tags = selectFilterTypeAdapter.selectedTags
                parentActivity.updateFilters(tags)
                requestArticleFlow()
            }
        }
    }

    override fun getFilters(): MutableList<ArticleTag> {
        return tags
    }

    override fun onStop() {
        super.onStop()
        parentActivity.showSearchBar(false, this)
        parentActivity.showFilters(false, this)
    }

    private fun moveToArticle(article:Article) {
        parentActivity.stopSearching()
        setTransition(SharedAxisTransition().setAxis(MaterialSharedAxis.Z))
        move(
            ArticlesListFragmentDirections.actionArticlesListFragmentToArticleFragment(
                article
            )
        )
    }
}
