package com.bryan.personalproject.ui.screens.leaderboard.viewModel

interface LeaderBoardViewModel {
    fun getScore()
    fun getScoreByQuizId(quizId: String)
}