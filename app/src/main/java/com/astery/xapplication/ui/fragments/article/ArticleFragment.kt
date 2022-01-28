package com.astery.xapplication.ui.fragments.article

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.astery.xapplication.R
import com.astery.xapplication.databinding.FragmentArticleBinding
import com.astery.xapplication.ui.adviceUtils.AdviceRenderer
import com.astery.xapplication.ui.fragments.XFragment
import com.astery.xapplication.ui.fragments.transitionHelpers.SharedAxisTransition
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


/**
 * menu -> select article -> Article
 * */
@AndroidEntryPoint
class ArticleFragment : XFragment() {
    private val binding: FragmentArticleBinding
        get() = bind as FragmentArticleBinding

    val viewModel: ArticleViewModel by viewModels()
    private var articleAdapter: ArticleAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTransition(SharedAxisTransition().setAxis(MaterialSharedAxis.Z))

        /* TODO(add arguments from news)
        arguments?.let {
            viewModel.selectedDay = it.getSerializable("article") as Article
        }
         */
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bind = FragmentArticleBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return bind.root
    }

    /** start position of slide gesture */
    private var downY = 0f

    /** is scrollView got to the end while Action_down */
    private var isScrollWasInTheEnd = false

    /** detect sliding between pages */
    @SuppressLint("ClickableViewAccessibility")
    override fun setListeners() {


        // maybe later make it dp
        val minSlideRange = 40

        binding.page.parent.setOnTouchListener { view, event ->
            view.onTouchEvent(event)
            // don't do anything while animation plays
            if (!isChangingPage) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        downY = event.y
                        isScrollWasInTheEnd = !binding.page.parent.canScrollVertically(1)
                    }
                    MotionEvent.ACTION_UP -> {
                        val upY = event.y

                        Timber.d("$downY $upY")

                        // scroll down and there is the end of the scroll view
                        if (downY >= (upY+minSlideRange) && isScrollWasInTheEnd) {
                            articleAdapter?.slidePage(true)
                        }
                        // scroll up and there is the start of the scroll view
                        else if (downY <= (upY-minSlideRange) && binding.page.parent.scrollY == 0) {
                            articleAdapter?.slidePage(false)
                        }
                    }
                    else -> {
                    }
                }
            }
            true
        }
    }

    override fun prepareAdapters() {
        articleAdapter = ArticleAdapter(1, requireContext())

        articleAdapter!!.listener = (object : ArticleAdapter.BlockListener {
            override fun onClick(oldPos: Int, newPos: Int) {
                fade(true, newPos > oldPos)?.doOnEnd {
                    if (newPos == 0) {
                        viewModel.selectArticle()
                    } else {
                        viewModel.selectItem(newPos - 1)
                    }
                }
            }
        })

        binding.pageSelector.adapter = articleAdapter
        binding.pageSelector.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

    }

    override fun setViewModelListeners() {
        viewModel.loadArticle(1)

        viewModel.article.observe(viewLifecycleOwner) {
            if (it.items != null) {
                articleAdapter!!.pageCount = it.items!!.size + 1
            }
        }
        viewModel.element.observe(viewLifecycleOwner) {
            fade(false)
            clearSpecialInfo()

            when (it) {
                is ArticlePresentable -> renderArticleInfo(it)
                is ItemPresentable -> renderItemInfo(it)
            }
        }
        // TODO(maybe... maybe... find a way to completely move in databind)
        viewModel.feedBackArticleStorage.observe(viewLifecycleOwner){
            binding.page.pageFeedback.feedBackStorage = viewModel.feedBackArticleStorage.value
        }
    }

    /** clear everything that may be created by renderArticleInfo or renderItemInfo */
    private fun clearSpecialInfo() {
        binding.page.tipsLayout.removeAllViews()
        binding.page.pageFeedback.root.isGone = true
    }

    /** render feetback page */
    private fun renderArticleInfo(article: ArticlePresentable) {
        binding.page.pageFeedback.root.isVisible = true
        binding.page.pageFeedback.feedBackStorage = viewModel.feedBackArticleStorage.value
    }

    /** render advices*/
    private fun renderItemInfo(item: ItemPresentable) {

        if (item.advices != null) {
            for (i in item.advices) {
                binding.page.tipsLayout.addView(
                    AdviceRenderer().render(
                        i,
                        viewModel.feedBackAdviceListener,
                        requireContext()
                    )
                )
            }
        }
    }

    override fun getFragmentTitle(): String {
        //TODO(add article name)
        return requireContext().resources.getString(R.string.title_new_event)
    }

    /** is the view scrolled down or up */
    private var moveDown = true
    private var isChangingPage = false

    /** animation between two pages (translationY + fade) */
    private fun fade(hide: Boolean, moveDown: Boolean): ValueAnimator? {
        // this line here because fade(hide = false) called just after loading screen from element.observe
        if (!hide && binding.page.parent.alpha == 1f) return null

        if (hide) isChangingPage = true

        this.moveDown = moveDown
        val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.duration = 250


        val startTranslationY = binding.page.parent.translationY
        val factor = 60
        valueAnimator.addUpdateListener {
            val value = it.animatedValue as Float

            if ((hide && moveDown) || (!hide && moveDown)) {
                binding.page.parent.translationY = startTranslationY - value * factor
            } else {
                binding.page.parent.translationY = startTranslationY + value * factor
            }

            if (!hide) {
                binding.page.parent.alpha = value
            } else {
                binding.page.parent.alpha = (1 - value)
            }
        }
        if (hide) {
            valueAnimator.doOnEnd {
                if (moveDown) {
                    binding.page.parent.translationY = startTranslationY + factor
                } else {
                    binding.page.parent.translationY = startTranslationY - factor
                }
            }
        } else {
            valueAnimator.doOnEnd { isChangingPage = false }
        }

        valueAnimator.start()
        return valueAnimator
    }

    fun fade(hide: Boolean) {
        fade(hide, moveDown)
    }
}
