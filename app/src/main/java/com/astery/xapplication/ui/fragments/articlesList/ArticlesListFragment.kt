package com.astery.xapplication.ui.fragments.articlesList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.astery.xapplication.databinding.FragmentCategoryBinding
import com.astery.xapplication.model.entities.GenderTag
import com.astery.xapplication.ui.activity.interfaces.SearchUsable
import com.astery.xapplication.ui.fragments.XFragment
import com.astery.xapplication.ui.fragments.transitionHelpers.SharedAxisTransition
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


/**
 * menu -> select article -> Article
 * */
@AndroidEntryPoint
class ArticlesListFragment : XFragment(), SearchUsable {
    private val binding: FragmentCategoryBinding
        get() = bind as FragmentCategoryBinding

    val viewModel: ArticlesListViewModel by viewModels()
    private var articleListAdapter: ArticlesListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTransition(SharedAxisTransition().setAxis(MaterialSharedAxis.Z))
        parentActivity.showSearchBar(true, this)
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
            viewModel.requestFlow("tips keyword", listOf(GenderTag.Woman)).collectLatest { source ->
                articleListAdapter?.submitData(source)
            }
        }
    }

    override fun getSearchText(value: String) {
        lifecycleScope.launch {
            viewModel.requestFlow(value, listOf()).collectLatest { source ->
                articleListAdapter?.submitData(source)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        parentActivity.showSearchBar(false, this)
    }
}
