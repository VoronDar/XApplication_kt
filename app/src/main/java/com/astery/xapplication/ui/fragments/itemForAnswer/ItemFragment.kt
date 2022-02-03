package com.astery.xapplication.ui.fragments.itemForAnswer

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.astery.xapplication.R
import com.astery.xapplication.databinding.PageItemBinding
import com.astery.xapplication.model.entities.Item
import com.astery.xapplication.ui.adviceUtils.AdviceRenderer
import com.astery.xapplication.ui.fragments.XFragment
import com.astery.xapplication.ui.fragments.article.ItemPresentable
import com.astery.xapplication.ui.fragments.article.Presentable
import com.astery.xapplication.ui.fragments.transitionHelpers.SharedAxisTransition
import com.astery.xapplication.ui.loadingState.LoadingErrorReason
import com.astery.xapplication.ui.loadingState.LoadingStateError
import com.astery.xapplication.ui.loadingState.LoadingStateLoading
import com.astery.xapplication.ui.loadingState.LoadingStateView
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import kotlin.math.roundToInt


/**
 * menu -> calendar fragment - advicesFragment - ItemFragment
 * */
@AndroidEntryPoint
class ItemFragment : XFragment() {
    private val binding: PageItemBinding
        get() = bind as PageItemBinding

    val viewModel: ItemViewModel by viewModels()
    var loadingState: LoadingStateView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTransition(SharedAxisTransition().setAxis(MaterialSharedAxis.Z))

        arguments?.let {
            viewModel.item = it.getParcelable("item") as? Item?
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bind = PageItemBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setRootPadding()
        binding.itemImage.isGone = true
        binding.pageFeedback.root.isGone = true
        super.onViewCreated(view, savedInstanceState)
    }


    override fun onPause() {
        super.onPause()
        loadingState?.doOnPauseUI()
    }

    override fun onResume() {
        super.onResume()
        loadingState?.doOnResumeUI()
    }

    override fun onStop() {
        super.onStop()
        loadingState?.doOnStopUI()
    }

    /** page_event used in two places. So, it must have different top padding in different places */
    private fun setRootPadding() {
        val typedValue = TypedValue()
        context?.theme?.resolveAttribute(
            android.R.attr.actionBarSize,
            typedValue,
            true
        )
        Timber.i("${typedValue.getDimension(DisplayMetrics()).roundToInt()}")
        binding.root.setPadding(
            0,
            typedValue.getDimension(context?.resources?.displayMetrics).roundToInt(),
            0,
            0
        )
    }

    override fun setViewModelListeners() {
        askForItemBody()
        viewModel.element.observe(viewLifecycleOwner) {
            Timber.d("result ${it.isSuccess}")
            if (it.isSuccess) {
                renderItemInfo(it.getOrThrow() as ItemPresentable)
                renderImage(it.getOrThrow())
                binding.parent.isVisible = true
                LoadingStateView.removeView()
            } else loadingState = LoadingStateView.addViewToViewGroup(LoadingStateError(it.exceptionOrNull()!! as LoadingErrorReason,::askForItemBody), layoutInflater, binding.frame)
        }
    }

    private fun askForItemBody(){
        loadingState = LoadingStateView.addViewToViewGroup(LoadingStateLoading(), layoutInflater, binding.frame)
        binding.parent.isGone = true
        viewModel.loadItemBody()
    }

    private fun renderImage(item:Presentable){
        binding.itemImage.isGone = item.image == null
        if (item.image != null) binding.itemImage.setImageBitmap(item.image)
    }

    /** render advices */
    private fun renderItemInfo(item: ItemPresentable) {
        Timber.i("item advices ${item.advices}")
        if (item.advices != null) {
            for (i in item.advices) {
                binding.tipsLayout.addView(
                    AdviceRenderer().render(
                        i,
                        viewModel.feedbackListener,
                        requireContext()
                    )
                )
            }
        }
    }

    // TODO(maybe change with different name)
    override fun getFragmentTitle() = getString(R.string.title_tips)
}
