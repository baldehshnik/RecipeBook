package com.firstapplication.recipebook.ui.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.firstapplication.recipebook.R
import com.firstapplication.recipebook.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private lateinit var inputMethodManager: InputMethodManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inputMethodManager =
            baseContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        setSupportActionBar(binding.appBarMain.toolbar)

        val navController = findNavController(R.id.homeHostFragment)

        val configuration = AppBarConfiguration(
            setOf(
                R.id.navHome, R.id.navHub, R.id.navAdding, R.id.navSearch, R.id.navRecipeInfo
            )
        )

        setupActionBarWithNavController(navController, configuration)
        binding.bottomNavView.setupWithNavController(navController)
    }

    override fun dispatchTouchEvent(motionEvent: MotionEvent?): Boolean {
        if (motionEvent != null && motionEvent.action == MotionEvent.ACTION_DOWN)
            inputMethodManager.hideSoftInputFromWindow(window.decorView.windowToken, 0)
        return super.dispatchTouchEvent(motionEvent)
    }
}