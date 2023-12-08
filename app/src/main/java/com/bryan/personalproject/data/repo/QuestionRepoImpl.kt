package com.bryan.personalproject.data.repo

import com.bryan.personalproject.core.service.AuthService
import com.bryan.personalproject.data.model.Question
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class QuestionRepoImpl(
    private val authService: AuthService,
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
): QuestionRepo {

    private fun getDbRef(): CollectionReference {
        val firebaseUser = authService.getCurrentUser()
        return firebaseUser?.uid?.let {
            db.collection("Quiz/$it/quiz")
        } ?: throw Exception("No authentic user found")
    }


    override suspend fun getQuestionsByQuizId(quizId: String): Question? {
        val query = getDbRef().whereEqualTo("quizId", quizId).get().await()

        return if (!query.isEmpty) {
            val doc = query.documents[0]
            doc.toObject(Question::class.java)?.copy(id = doc.id)
        } else {
            null
        }
    }

}