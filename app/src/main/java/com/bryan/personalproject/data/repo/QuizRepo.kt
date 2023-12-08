package com.bryan.personalproject.data.repo

import com.bryan.personalproject.data.model.Quiz
import kotlinx.coroutines.flow.Flow

interface QuizRepo {

    suspend fun getAllQuiz(): Flow<List<Quiz>>

    suspend fun addQuiz(quiz: Quiz)

    suspend fun getQuizById(id: String): Quiz?

}