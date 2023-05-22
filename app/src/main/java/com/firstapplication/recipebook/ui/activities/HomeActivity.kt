package com.firstapplication.recipebook.ui.activities

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.firstapplication.recipebook.R
import com.firstapplication.recipebook.databinding.ActivityHomeBinding
import com.firstapplication.recipebook.extensions.appComponent
import javax.inject.Inject

class HomeActivity : AppCompatActivity() {

    @Inject
    lateinit var inputMethodManager: InputMethodManager

    override fun onCreate(savedInstanceState: Bundle?) {
        applicationContext.appComponent.inject(this)
        super.onCreate(savedInstanceState)
        val binding = ActivityHomeBinding.inflate(layoutInflater).also { setContentView(it.root) }

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

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) hideKeyboard()
        return super.dispatchTouchEvent(ev)
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
    }
}