package com.astery.xapplication.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.astery.xapplication.R
import com.astery.xapplication.ui.activity.interfaces.ParentActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

/**
 * single activity
 * */
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ParentActivity {

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
}