package com.bryan.personalproject.data.repo

import com.bryan.personalproject.data.model.Score
import kotlinx.coroutines.flow.Flow

interface ScoreRepo {
    suspend fun addResult(score: Score)
    suspend fun getResult() : Flow<List<Score>>
    suspend fun getResultByQuizId(quizId: String): Flow<List<Score>>
}