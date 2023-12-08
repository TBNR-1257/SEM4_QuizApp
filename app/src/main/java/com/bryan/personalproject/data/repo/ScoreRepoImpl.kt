package com.bryan.personalproject.data.repo

import android.util.Log
import com.bryan.personalproject.core.service.AuthService
import com.bryan.personalproject.data.model.Score
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ScoreRepoImpl(
    private val authService: AuthService,
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
): ScoreRepo {
    private fun getDbRef(): CollectionReference {
        return db.collection("Results")
    }

    override suspend fun addResult(score: Score) {
        getDbRef().document().set(score.toHashMap()).await()
    }

    override suspend fun getResult() = callbackFlow {
        val listener = getDbRef().addSnapshotListener { value, error ->
            if (error != null) {
                throw error
            }
            val score = mutableListOf<Score>()
            value?.documents?.let { docs ->
                for (doc in docs) {
                    doc.data?.let {
                        it["id"] = doc.id
                        score.add(Score.fromHashMap(it))
                    }
                }
                trySend(score)
            }
        }
        awaitClose {
            listener.remove()
        }
    }


    override suspend fun getResultByQuizId(quizId: String) = callbackFlow {
        val listener = getDbRef().whereEqualTo("quizId", quizId).addSnapshotListener { value, error ->
            if (error != null) {
                throw error
            }
            val score = mutableListOf<Score>()
            value?.documents?.let { docs ->
                for (doc in docs) {
                    doc.data?.let {
                        it["id"] = doc.id
                        score.add(Score.fromHashMap(it))
                    }
                }
//                Log.d("ScoreRepoImpl", "Scores for Quiz ID: $score")

                trySend(score)
            }
        }
        awaitClose {
            listener.remove()
        }
    }

}