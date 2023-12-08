package com.bryan.personalproject.ui.screens.addquiz.viewModel

import kotlinx.coroutines.flow.SharedFlow

interface AddQuizViewModel {
    val finish: SharedFlow<Unit>
    fun addQuiz(QuizId: String, title: String, timer: Long?)
    fun getCurrentUser()
    fun readCsv(lines:List<String>)
}