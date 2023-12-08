package com.bryan.personalproject.ui.screens.joinquiz.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bryan.personalproject.data.model.Quiz
import com.bryan.personalproject.data.repo.QuizRepo
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
class JoinQuizViewModelImpl @Inject constructor(
    private val quizRepo: QuizRepo
) : BaseViewModel(), JoinQuizViewModel {

    private val _quiz: MutableStateFlow<Quiz> = MutableStateFlow(Quiz(questionTitles = emptyList(), options = emptyList(), answers = emptyList(), timeLimit = 0))
    val quiz: StateFlow<Quiz> = _quiz

    override fun getQuiz(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
               quizRepo.getQuizById(id)
            }?.let {
                _quiz.value = it
            }
        }
    }

}