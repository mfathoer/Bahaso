package com.bahaso.bahaso

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bahaso.bahaso.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.bottomNavigationView
        val navControllerCompat = findNavController(R.id.navHostFragment)

        navView.setupWithNavController(navControllerCompat)

        navControllerCompat.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.signUpFragment -> navView.visibility = View.GONE
                R.id.profileFragment -> navView.visibility = View.VISIBLE
                R.id.homeFragment -> navView.visibility = View.VISIBLE
                R.id.loginFragment -> navView.visibility = View.GONE
                R.id.leaderBoardFragment -> navView.visibility = View.VISIBLE
                R.id.quizFragment -> navView.visibility = View.GONE
                R.id.scoreFragment -> navView.visibility = View.GONE
                R.id.theoryFragment -> navView.visibility = View.GONE
                R.id.editBiodataFragment -> navView.visibility = View.GONE
            }
        }
    }
}