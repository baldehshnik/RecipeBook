package com.firstapplication.recipebook.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.firstapplication.recipebook.R
import com.firstapplication.recipebook.databinding.ActivityHomeBinding
import com.firstapplication.recipebook.databinding.AppToolbarBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val navController = findNavController(R.id.homeHostFragment)

        val configuration = AppBarConfiguration(
            setOf(
                R.id.navHome, R.id.navHub
            )
        )

        setupActionBarWithNavController(navController, configuration)
        binding.bottomNavView.setupWithNavController(navController)
    }
}