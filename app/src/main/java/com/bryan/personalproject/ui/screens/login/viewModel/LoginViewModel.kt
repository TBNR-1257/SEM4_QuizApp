package com.bryan.personalproject.ui.screens.login.viewModel

interface LoginViewModel {
    fun login(email: String, pass: String)
    fun getCurrentUser()
}