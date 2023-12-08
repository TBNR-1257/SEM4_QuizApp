package com.bryan.personalproject.ui.screens.teacherdash.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.bryan.personalproject.core.service.AuthService
import com.bryan.personalproject.data.model.Quiz
import com.bryan.personalproject.data.model.User
import com.bryan.personalproject.data.repo.QuizRepo
import com.bryan.personalproject.data.repo.UserRepo
import com.bryan.personalproject.ui.screens.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeacherDashViewModelImpl @Inject constructor(
    private val quizRepo: QuizRepo,
    private val authService: AuthService,
    private val userRepo: UserRepo
): BaseViewModel(), TeacherDashViewModel {

    private val _user = MutableStateFlow(User(name = "Unknown", email = "Unknown", role = "Unknown"))
    val user: StateFlow<User> = _user

    private val _quizs = MutableStateFlow<List<Quiz>>(
        emptyList()
    )
    override val quiz: StateFlow<List<Quiz>> = _quizs

    init {
        getQuiz()
        getCurrentUser()
    }


    override fun getQuiz() {
        viewModelScope.launch(Dispatchers.IO) {
            quizRepo.getAllQuiz().collect() {
                _quizs.value = it
            }
        }
    }

    override fun getCurrentUser() {
        val firebaseUser = authService.getCurrentUser()
        Log.d("debugging", firebaseUser?.uid.toString())
        firebaseUser?.let {
            viewModelScope.launch(Dispatchers.IO) {
                errorHandler { userRepo.getUser(it.uid) }?.let {  user ->
                    Log.d("debugging", user.toString())
                    _user.value = user
                }
            }
        }
    }

}