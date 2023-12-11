package com.bryan.personalproject.ui.screens.login.viewModel

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.bryan.personalproject.core.service.AuthService
import com.bryan.personalproject.data.model.User
import com.bryan.personalproject.data.repo.UserRepo
import com.bryan.personalproject.ui.screens.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModelImpl @Inject constructor(
    private val authService: AuthService,
    private val userRepo: UserRepo,
): BaseViewModel(), LoginViewModel {

    private val _navigateToStudentDash = MutableSharedFlow<Unit>()
    val navigateToStudentDash: SharedFlow<Unit> get() = _navigateToStudentDash

    private val _navigateToTeacherDash = MutableSharedFlow<Unit>()
    val navigateToTeacherDash: SharedFlow<Unit> get() = _navigateToTeacherDash

    private val _user = MutableSharedFlow<User>()
    val user: SharedFlow<User> = _user


    override fun login(email: String, pass: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = errorHandler {
                authService.login(email, pass)
            }
            if (result != null) {
//                getCurrentUser()
                _success.emit("Login Successful")
            }
        }
    }


    override fun getCurrentUser() {
        val firebaseUser = authService.getCurrentUser()
        Log.d("debugging", firebaseUser?.uid.toString())
        firebaseUser?.let {
            viewModelScope.launch(Dispatchers.IO) {
                errorHandler { userRepo.getUser(it.uid) }?.let {  user ->
                    Log.d("debugging", user.toString())
                    _user.emit(user)
//                    navigateBasedOnRole(user.role)
                }
            }
        }
    }


}

