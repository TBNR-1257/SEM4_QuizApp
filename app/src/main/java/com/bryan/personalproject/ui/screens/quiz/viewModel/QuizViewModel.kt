package com.bryan.personalproject.ui.screens.quiz.viewModel

import com.bryan.personalproject.data.model.Quiz
import com.bryan.personalproject.data.model.User
import kotlinx.coroutines.flow.StateFlow

interface QuizViewModel {
    fun getQuiz(id: String)
    fun startCountdownTimer(timeLimit: Long)
    fun getCurrentUser()
    fun addResult(result: String, quizId: String)
}
