package com.bryan.personalproject.data.repo

import com.bryan.personalproject.core.service.AuthService
import com.bryan.personalproject.data.model.User
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepoImpl(
    private val authService: AuthService,
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
): UserRepo {

    private fun getDbRef(): CollectionReference {
        return db.collection("users")
    }

    private fun getUid(): String {
        val firebaseUser = authService.getCurrentUser()
        return firebaseUser?.uid ?: throw Exception("No authorized user found")
    }

    override suspend fun addUser(user: User) {
        getDbRef().document(getUid()).set(user.toHashMap()).await()
    }

    override suspend fun getUser(uid: String): User? {
        val doc = getDbRef().document(getUid()).get().await()
        return doc.data?.let {
            it["id"] = getUid()
            User.fromHashMap(it)
        }
    }

    override suspend fun removeUser() {
        getDbRef()
    }

    override suspend fun updateUser(user: User) {
        getDbRef().document(getUid()).set(user.toHashMap())
    }


}