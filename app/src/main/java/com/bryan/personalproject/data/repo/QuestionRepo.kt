package com.bryan.personalproject.data.repo

import com.bryan.personalproject.data.model.Question

interface QuestionRepo {

    suspend fun getQuestionsByQuizId(quizId: String): Question?
}