package com.astery.xapplication.ui.fragments.articlesList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.insertHeaderItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.astery.xapplication.databinding.FragmentCategoryBinding
import com.astery.xapplication.model.entities.Article
import com.astery.xapplication.model.entities.GenderTag
import com.astery.xapplication.ui.fragments.XFragment
import com.astery.xapplication.ui.fragments.transitionHelpers.SharedAxisTransition
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber


/**
 * menu -> select article -> Article
 * */
@AndroidEntryPoint
class ArticlesListFragment : XFragment() {
    private val binding: FragmentCategoryBinding
        get() = bind as FragmentCategoryBinding

    val viewModel: ArticlesListViewModel by viewModels()
    private var articleListAdapter: ArticlesListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTransition(SharedAxisTransition().setAxis(MaterialSharedAxis.Z))
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
            ArticlesListAdapter()
        binding.recyclerView.adapter = articleListAdapter
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
    }

    override fun getFragmentTitle(): String? {
        return null
    }

    override fun setViewModelListeners() {
        prepareAdapters()
        lifecycleScope.launch {
            //viewModel.setArticleFlow(listOf(GenderTag.Man.id))
            viewModel.requestFlow("", listOf()).collectLatest { source ->
                articleListAdapter?.submitData(source)
            }
        }
    }
}
