package com.bryan.personalproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.bryan.personalproject.core.service.AuthService
import com.bryan.personalproject.data.model.User
import com.bryan.personalproject.data.repo.UserRepo
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity: AppCompatActivity() {

    @Inject
    lateinit var authService: AuthService

    @Inject
    lateinit var userRepo: UserRepo

    private lateinit var navController: NavController

    lateinit var bottomNavigationView: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val role = intent.getStringExtra("role")

        navController = findNavController(R.id.navHostFragment)

        if(role == "Teacher") {
            navController.popBackStack(R.id.studentDashFragment, true)
            navController.navigate(R.id.teacherDashFragment)
        }


        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)

        bottomNavigationView.setOnItemSelectedListener{
            val id = when(it.itemId) {
                R.id.home -> if(role == "Student") R.id.studentDashFragment else R.id.teacherDashFragment
                R.id.leaderboard -> R.id.leaderboardFragment
                R.id.profile -> R.id.profileFragment
                else -> R.id.profileFragment
            }
            navController.navigate(id)
            true
        }


        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.quizFragment, R.id.addQuizFragment, R.id.joinQuizFragment -> {
                    bottomNavigationView.visibility = View.GONE
                }
                else -> {
                    bottomNavigationView.visibility = View.VISIBLE
                }
            }
        }
    }
}