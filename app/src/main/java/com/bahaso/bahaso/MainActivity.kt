package com.bahaso.bahaso

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bahaso.bahaso.databinding.ActivityMainBinding
import com.bahaso.bahaso.quiz.QuizFragmentDirections
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navControllerCompat: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.bottomNavigationView
        navControllerCompat = findNavController(R.id.navHostFragment)

        navView.setupWithNavController(navControllerCompat)

        navControllerCompat.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.signUpFragment -> navView.visibility = View.GONE
                R.id.profileFragment -> navView.visibility = View.VISIBLE
                R.id.homeFragment -> navView.visibility = View.VISIBLE
                R.id.loginFragment -> navView.visibility = View.GONE
                R.id.tempFragment -> navView.visibility = View.VISIBLE
                R.id.quizFragment -> navView.visibility = View.GONE
                R.id.theoryFragment -> navView.visibility = View.GONE
            }
        }
    }

    override fun onBackPressed() {
        if (navControllerCompat.currentDestination?.id == R.id.quizFragment) {
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle(getString(R.string.alert_title_cancel_quiz))
            alertDialog.setMessage(getString(R.string.alert__message_cancel_quiz))
            alertDialog.setPositiveButton(getString(R.string.yes)) { _, _ ->
                val action = QuizFragmentDirections.actionQuizFragmentToHomeFragment2()
                navControllerCompat.navigate(action)
            }
            alertDialog.setNegativeButton(getString(R.string.no)) { _, _ -> }
            alertDialog.show()
        } else {
            super.onBackPressed()
        }

    }
}