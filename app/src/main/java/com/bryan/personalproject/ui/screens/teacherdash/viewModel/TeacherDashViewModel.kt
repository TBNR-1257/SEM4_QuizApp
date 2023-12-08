package com.bryan.personalproject.ui.screens.teacherdash.viewModel

import com.bryan.personalproject.data.model.Quiz
import kotlinx.coroutines.flow.StateFlow

interface TeacherDashViewModel {

    val quiz: StateFlow<List<Quiz>>

    fun getQuiz()
    fun getCurrentUser()

}