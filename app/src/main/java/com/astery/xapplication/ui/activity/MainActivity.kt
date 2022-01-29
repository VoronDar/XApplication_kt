package com.astery.xapplication.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isGone
import androidx.core.view.iterator
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.astery.xapplication.R
import com.astery.xapplication.model.entities.ArticleTag
import com.astery.xapplication.ui.activity.interfaces.FiltersUsable
import com.astery.xapplication.ui.activity.interfaces.ParentActivity
import com.astery.xapplication.ui.activity.interfaces.SearchUsable
import com.astery.xapplication.ui.activity.popupDialogue.Blockable
import com.astery.xapplication.ui.activity.popupDialogue.BlockableView
import com.astery.xapplication.ui.activity.popupDialogue.DialoguePanel
import com.astery.xapplication.ui.activity.popupDialogue.PanelHolder
import com.astery.xapplication.ui.activity.spechToText.SpeechToText
import com.astery.xapplication.ui.fragments.articlesList.FiltersAdapter
import com.astery.xapplication.ui.fragments.calendar.CalendarFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


/**
 * single activity
 * */
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ParentActivity, PanelHolder {

    // TODO (make custom behaviour onBack)

    private var _navController: NavController? = null
    private val navController
        get() = _navController!!
    private var _bottomView: BottomNavigationView? = null
    private val bottomView
        get() = _bottomView!!

    private val speechToText: SpeechToText = SpeechToText(this)
    private val infoPanel = DialoguePanel(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.hostFragment) as NavHostFragment
        _navController = navHostFragment.navController

        setSupportActionBar(findViewById(R.id.toolbar)!!)

        _bottomView = findViewById(R.id.bottom_navigation)
        bottomView.setupWithNavController(navController)

        doSearchCommands()
        doFilterCommands()
    }


    override fun changeTitle(title: String?) {
        supportActionBar?.title = title
        Timber.i(supportActionBar?.title.toString())
    }


    // MARK: Toolbar changing
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        if (menuMonthListener != null) showMenuNav(!menuMonthListener!!.close, menuMonthListener!!)
        setMenuListeners(menu)
        return super.onCreateOptionsMenu(menu)
    }

    /** hide or reveal iconButtons for calendar **/
    override fun showMenuNav(show: Boolean, listener: CalendarFragment.MenuNavListener) {
        menuMonthListener = listener
        val toolBar = findViewById<Toolbar>(R.id.toolbar)
        if (toolBar?.menu?.findItem(R.id.action_back) != null) {
            toolBar.menu?.findItem(R.id.action_back)?.isVisible = show
            toolBar.menu?.findItem(R.id.action_forward)?.isVisible = show
        }
    }


    private fun setMenuListeners(menu: Menu?) {
        menu?.findItem(R.id.action_back)?.setOnMenuItemClickListener {
            menuMonthListener?.click(true)
            return@setOnMenuItemClickListener true
        }
        menu?.findItem(R.id.action_forward)?.setOnMenuItemClickListener {
            menuMonthListener?.click(false)
            return@setOnMenuItemClickListener true
        }
    }


    private var menuMonthListener: CalendarFragment.MenuNavListener? = null

    private val searchCommands: ArrayList<SearchCommand> = arrayListOf()
    private val filterCommands: ArrayList<FilterCommand> = arrayListOf()
    override fun showSearchBar(show: Boolean, fragment: SearchUsable) {
        try {
            findViewById<View>(R.id.search).isGone = !show
        } catch (e: Exception) {
            // activity is not created (I tried to use lifeCycle.currentState, but it doesn't suit this case)
            searchCommands.add(SearchCommand(show, fragment))
            return
        }
        if (speechToText.isRecognitionAvailable()) {
            findViewById<View>(R.id.voice).isGone = !show
            findViewById<View>(R.id.voice).setOnClickListener {
                speechToText.speak()
            }
        }
        findViewById<View>(R.id.search).setOnClickListener {
            fragment.getSearchText(findViewById<EditText>(R.id.search_text)!!.text.toString())
        }
        findViewById<View>(R.id.search_text).isGone = !show
        findViewById<View>(R.id.toolbar).isGone = show
        speechToText.fragment = fragment

        // TODO (if I handle done, it doesn't close, but if i don't, i can't search)

        findViewById<EditText>(R.id.search_text).setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                findViewById<View>(R.id.search).performClick()
                false
            } else false
        }
    }


    var fAdapter: FiltersAdapter? = null
    override fun showFilters(show: Boolean, fragment: FiltersUsable) {
        try {
            findViewById<View>(R.id.block_filters).isGone = !show
        } catch (e: Exception) {
            // activity is not created (I tried to use lifeCycle.currentState, but it doesn't suit this case)
            filterCommands.add(FilterCommand(show, fragment))
            return
        }

        fAdapter = FiltersAdapter(fragment.getFilters(), this)
        val recyclerView = findViewById<RecyclerView>(R.id.filters)
        recyclerView.adapter = fAdapter
        recyclerView.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        findViewById<View>(R.id.add_filter).setOnClickListener {
            infoPanel.openPanel(fragment.getDialogueHolder(), fragment.getBlockable())
        }

    }

    override fun updateFilters(list: List<ArticleTag>) {
        fAdapter?.units = list
    }

    private fun doSearchCommands() {
        while (searchCommands.isNotEmpty()) {
            showSearchBar(searchCommands[0].show, searchCommands[0].fragment)
            searchCommands.removeAt(0)
        }
    }

    private fun doFilterCommands() {
        while (filterCommands.isNotEmpty()) {
            showFilters(filterCommands[0].show, filterCommands[0].fragment)
            filterCommands.removeAt(0)
        }
    }

    fun getSearchText(value: String) {
        findViewById<EditText>(R.id.search_text)!!.setText(value)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        speechToText.acceptActivityResult(requestCode, resultCode, data)
    }

    override fun getContext(): Context {
        return applicationContext
    }

    override fun findView(id: Int): View {
        return findViewById(id)
    }

    override fun getBlockable(): List<Blockable> {
        return listOf(BlockableView(findViewById(R.id.toolbar)),
            BlockableView(findViewById(R.id.add_filter)),
            BlockableView(findViewById(R.id.search)),
            BlockableView(findViewById(R.id.voice)),
            BlockableView(findViewById(R.id.search_text)),
            object : Blockable {
                override fun setEnabled(enable: Boolean) {
                    for (i in bottomView.menu) {
                        i.isEnabled = enable
                    }
                }
            }
        )

    }

    override fun onBackPressed() {
        if (DialoguePanel.isOpened) infoPanel.closePanel()
        else super.onBackPressed()
    }

    data class SearchCommand(val show: Boolean, val fragment: SearchUsable)
    data class FilterCommand(val show: Boolean, val fragment: FiltersUsable)
}