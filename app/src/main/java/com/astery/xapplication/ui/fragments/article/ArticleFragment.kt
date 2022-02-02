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
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.astery.xapplication.R
import com.astery.xapplication.databinding.FragmentArticleBinding
import com.astery.xapplication.model.entities.Article
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

        arguments?.let { bundle ->
            bundle.getParcelable<Article>("article")?.let { value -> viewModel.setArticle(value) }
        }
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
                        if (downY >= (upY + minSlideRange) && isScrollWasInTheEnd) {
                            articleAdapter?.slidePage(true)
                        }
                        // scroll up and there is the start of the scroll view
                        else if (downY <= (upY - minSlideRange) && binding.page.parent.scrollY == 0) {
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

        viewModel.article.observe(viewLifecycleOwner) {
            if (it.items != null) {
                articleAdapter!!.pageCount = it.items!!.size + 1
            }
            setTitle()
        }
        viewModel.element.observe(viewLifecycleOwner) { result->
            fadeAway()

            if (result.isFailure){
                // TODO(I have no idea what to do in this case)
                return@observe
            }

            val presentable = result.getOrThrow()

            renderImage(presentable)
            clearSpecialInfo()
            when (presentable) {
                is ArticlePresentable -> renderArticleInfo(presentable)
                is ItemPresentable -> renderItemInfo(presentable)
            }
        }
        // TODO(maybe... maybe... find a way to completely move in databind)
        viewModel.feedBackArticleStorage.observe(viewLifecycleOwner) {
            binding.page.pageFeedback.feedBackStorage = viewModel.feedBackArticleStorage.value
        }
    }

    private fun renderImage(presentable: Presentable) {
        if (presentable.image != null) binding.page.itemImage.setImageBitmap(presentable.image!!)
        else binding.page.itemImage.setImageDrawable(
            ContextCompat.getDrawable(
                context!!,
                R.drawable.dating
            )
        )
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

    override fun getFragmentTitle(): String? {
        return viewModel.article.value?.name
    }

    /** is the view scrolled down or up */
    private var moveDown = true
    private var isChangingPage = false

    /** animation between two pages (translationY + fade) */
    private fun fade(hide: Boolean, moveDown: Boolean): ValueAnimator? {
        // this line here because fade(hide = false) called just after loading screen from element.observe
        if (!hide && binding.page.parent.alpha == 1f) return null

        if (isChangingPage) return null

        isChangingPage = true

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
                isChangingPage = false
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

    private fun fadeAway() {
        fade(false, moveDown)
    }

    companion object {
        @JvmStatic
        fun newInstance() = ArticleFragment()
    }
}
