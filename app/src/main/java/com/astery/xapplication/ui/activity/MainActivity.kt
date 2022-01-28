package com.astery.xapplication.ui.activity

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.Menu
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isGone
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.astery.xapplication.R
import com.astery.xapplication.ui.activity.interfaces.ParentActivity
import com.astery.xapplication.ui.activity.interfaces.SearchUsable
import com.astery.xapplication.ui.fragments.calendar.CalendarFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.lang.Exception

/**
 * single activity
 * */
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ParentActivity {

    // TODO (make custom behaviour onBack)

    private var _navController:NavController? = null
    private val navController
        get() = _navController!!

    private val speechToText:SpeechToText = SpeechToText(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.hostFragment) as NavHostFragment
        _navController = navHostFragment.navController

        setSupportActionBar(findViewById(R.id.toolbar)!!)

        val bottomView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomView.setupWithNavController(navController)

        doSearchCommands()
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
        if (toolBar?.menu?.findItem(R.id.action_back) != null){
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

    private val searchCommands:ArrayList<SearchCommand> = arrayListOf()
    override fun showSearchBar(show: Boolean, fragment: SearchUsable) {
            try {
                findViewById<View>(R.id.search).isGone = !show
            } catch (e:Exception) {
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
    }

    private fun doSearchCommands(){
        while (searchCommands.isNotEmpty()){
            showSearchBar(searchCommands[0].show, searchCommands[0].fragment)
            searchCommands.removeAt(0)
        }
    }

    fun getSearchText(value:String){
        findViewById<EditText>(R.id.search_text)!!.setText(value)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        speechToText.acceptActivityResult(requestCode, resultCode, data)
    }
}


data class SearchCommand(val show: Boolean, val fragment: SearchUsable)