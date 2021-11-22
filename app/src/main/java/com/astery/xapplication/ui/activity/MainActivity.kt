package com.astery.xapplication.ui.activity

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.astery.xapplication.R
import com.astery.xapplication.ui.activity.interfaces.ParentActivity
import com.astery.xapplication.ui.fragments.calendar.CalendarFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

/**
 * single activity
 * */
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ParentActivity {

    // TODO (make custom behaviour onBack)

    private var _navController:NavController? = null
    private val navController
        get() = _navController!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.hostFragment) as NavHostFragment
        _navController = navHostFragment.navController

        setSupportActionBar(findViewById(R.id.toolbar)!!)

        val bottomView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomView.setupWithNavController(navController)
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
}