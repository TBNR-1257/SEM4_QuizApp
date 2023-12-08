package com.bryan.personalproject.ui.screens.register.viewModel

import androidx.lifecycle.viewModelScope
import com.bryan.personalproject.core.service.AuthService
import com.bryan.personalproject.data.model.User
import com.bryan.personalproject.data.repo.UserRepo
import com.bryan.personalproject.ui.screens.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModelImpl @Inject constructor(
    private val authService: AuthService,
    private val userRepo: UserRepo
): BaseViewModel(), RegisterViewModel {

    override fun register(name: String, email: String, pass: String, confirmPass: String, role: String) {
        viewModelScope.launch {
            val user = errorHandler {
                authService.register(email, pass)
            }

            if(user != null) {
                _success.emit("Registered Successfully")
                errorHandler {
                    userRepo.addUser(
                        User(name = name, email = email, role = role)
                    )
                }
            }
        }
    }

}