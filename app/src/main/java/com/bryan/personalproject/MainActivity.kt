package com.bryan.personalproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
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


        navController = findNavController(R.id.navHostFragment)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)


        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment, R.id.registerFragment, R.id.quizFragment, R.id.addQuizFragment, R.id.joinQuizFragment -> {
                    bottomNavigationView.visibility = View.GONE
                }
                else -> {
                    bottomNavigationView.visibility = View.VISIBLE
                }
            }
        }
    }

    fun setupNavigation() {
        lifecycleScope.launch {
            val currentUser = authService.getCurrentUser()
            val user = currentUser?.let { userRepo.getUser(it.uid) }

            bottomNavigationView.setOnItemSelectedListener{
                val id = when(it.itemId) {
                    R.id.home -> if(user?.role == "Student") R.id.studentDashFragment else R.id.teacherDashFragment
                    R.id.leaderboard -> R.id.leaderboardFragment
                    R.id.profile -> R.id.profileFragment
                    else -> R.id.profileFragment
                }
                navController.navigate(id)
                true
            }
        }
    }


}