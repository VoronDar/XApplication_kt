package com.astery.xapplication.ui.fragments.articlesList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
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
import com.astery.xapplication.ui.fragments.transitionHelpers.SharedAxisTransition
import com.astery.xapplication.ui.loadingState.LoadingStateLoading
import com.astery.xapplication.ui.loadingState.LoadingStateNothing
import com.astery.xapplication.ui.loadingState.LoadingStateView
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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

    private var keywords: String = ""
    private var tags: MutableList<ArticleTag> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTransition(SharedAxisTransition().setAxis(MaterialSharedAxis.Z))

        arguments?.let { bundle ->
            bundle.getString("keyword")?.let { value -> keywords = value }
            bundle.getIntArray("tags")?.let { value ->
                tags = ArticleTag.convertFromIdList(value.toList())
                parentActivity.updateFilters(tags)
                Timber.d("tags $tags")
            }
        }
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


    private var loadingState: LoadingStateView? = null
    override fun onPause() {
        super.onPause()
        loadingState?.doOnPauseUI()
    }

    override fun onResume() {
        super.onResume()
        loadingState?.doOnResumeUI()
    }



    override fun prepareAdapters() {
        setRootPadding()
        if (articleListAdapter != null) {
            binding.recyclerView.adapter = articleListAdapter!!
            return
        }
        articleListAdapter =
            ArticlesListAdapter(viewModel, binding.recyclerView)
        binding.recyclerView.adapter = articleListAdapter
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        articleListAdapter!!.blockListener = object : ArticlesListAdapter.BlockListener {
            override fun onClick(article: Article) {
                moveToArticle(article)
            }
        }
    }

    /** page_event used in two places. So, it must have different top padding in different places */
    private fun setRootPadding() {
        binding.root.setPadding(
            0,
            80,
            0,
            0
        )
    }



    override fun getFragmentTitle(): String? {
        return null
    }

    override fun setViewModelListeners() {
        prepareAdapters()
        requestArticleFlow()

        viewModel.articlesFlow.observe(viewLifecycleOwner) { flow ->
            lifecycleScope.launch {
                flow?.collectLatest { source ->
                    articleListAdapter?.addOnPagesUpdatedListener {
                        if (articleListAdapter?.itemCount == 0) {
                            loadingState = LoadingStateView.addViewToViewGroup(
                                LoadingStateNothing(),
                                layoutInflater,
                                binding.frame
                            )
                        } else {
                            LoadingStateView.removeView()
                            binding.recyclerView.isVisible = true
                        }
                    }
                    articleListAdapter?.submitData(source)
                }
            }
        }
    }

    // а вот тут у нас обитает дикий костыль. Если вернуться назад на страницу paging adapter откажется показывать что-либо вообще
    // чтобы это хоть как-то сгладить я просто начинаю новый фрагмент с теми же настройками
    private var isStarted = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (!isStarted) super.onViewCreated(view, savedInstanceState)
        if (isStarted) {
            move(
                ArticlesListFragmentDirections.articleListReload(
                    ArticleTag.convertToIdList(tags).toIntArray(), keywords
                )
            )
        }
        isStarted = true

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
        Timber.d("request")
        articleListAdapter?.triedToLoadImage?.clear()

        binding.recyclerView.isGone = true
        loadingState = LoadingStateView.addViewToViewGroup(
            LoadingStateLoading(),
            layoutInflater,
            binding.frame
        )
        viewModel.requestFlow(keywords, tags)
    }


    override fun getBlockable(): List<Blockable> {
        return listOf(articleListAdapter!!)
    }

    override fun getDialogueHolder(): DialogueHolder {
        val selectFilterTypeAdapter = SelectFilterTypeAdapter(null, requireContext())
        return object : DialogueHolder {

            override fun getBinding(
                inflater: LayoutInflater,
                container: ViewGroup?,
                onClose: () -> Unit
            ): ViewDataBinding {
                val binding = DialogFilterBinding.inflate(inflater, container, false)
                binding.accept.setOnClickListener {
                    onClose()
                }

                selectFilterTypeAdapter.selectedTags = tags
                selectFilterTypeAdapter.units = ArticleTagType.values().toList()
                binding.recyclerView.adapter = selectFilterTypeAdapter
                binding.recyclerView.layoutManager =
                    LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                binding.recyclerView.isNestedScrollingEnabled = false
                binding.recyclerView.overScrollMode = RecyclerView.OVER_SCROLL_NEVER

                return binding
            }

            override fun onClosePanel() {
                setFilters(selectFilterTypeAdapter.selectedTags)
                parentActivity.updateFilters(tags)
            }
        }
    }

    override fun getFilters(): MutableList<ArticleTag> {
        return tags
    }

    override fun onStop() {
        super.onStop()
        LoadingStateView.removeView()
        parentActivity.showSearchBar(false, this)
        parentActivity.showFilters(false, this)
    }

    private fun moveToArticle(article: Article) {
        parentActivity.hideSearchKeyboard()
        setTransition(SharedAxisTransition().setAxis(MaterialSharedAxis.Z))
        move(
            ArticlesListFragmentDirections.actionArticlesListFragmentToArticleFragment(
                article
            )
        )
    }
}
