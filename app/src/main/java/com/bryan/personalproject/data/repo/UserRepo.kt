package com.bryan.personalproject.data.repo

import com.bryan.personalproject.data.model.User
import com.google.firebase.auth.FirebaseUser

interface UserRepo {
    suspend fun addUser(user: User)
    suspend fun getUser(uid: String): User?
    suspend fun removeUser()
    suspend fun updateUser(user: User)
}