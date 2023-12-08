package com.bryan.personalproject.ui.screens.addquiz.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bryan.personalproject.core.service.AuthService
import com.bryan.personalproject.data.model.Question
import com.bryan.personalproject.data.model.Quiz
import com.bryan.personalproject.data.model.User
import com.bryan.personalproject.data.repo.QuizRepo
import com.bryan.personalproject.data.repo.UserRepo
import com.bryan.personalproject.ui.screens.addquiz.viewModel.AddQuizViewModel
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
class AddQuizViewModelImpl @Inject constructor(
    private val authService: AuthService,
    private val quizRepo: QuizRepo,
    private val userRepo: UserRepo
) : BaseViewModel(), AddQuizViewModel {
    private val _finish = MutableSharedFlow<Unit>()
    override val finish: SharedFlow<Unit> = _finish

    private val _quizQuestion = MutableStateFlow<List<Question>>(emptyList())
    val quizQuestion: StateFlow<List<Question>> = _quizQuestion

    private val _user = MutableStateFlow(User(name = "Unknown", email = "Unknown", role = "Unknown"))
    val user: StateFlow<User> = _user

    init {
        getCurrentUser()
    }

    override fun addQuiz(QuizId: String, title: String,  timer: Long?) {
        viewModelScope.launch(Dispatchers.IO) {
            val questionTitles = mutableListOf<String>()
            val options = mutableListOf<String>()
            val answers = mutableListOf<String>()
            _quizQuestion.value.map {
                questionTitles.add(it.question)
                options.add(it.option1)
                options.add(it.option2)
                options.add(it.option3)
                options.add(it.option4)
                answers.add(it.correctAnswer)
            }
            errorHandler {
                quizRepo.addQuiz(
                    Quiz(QuizId = QuizId,
                        title = title,
                        questionTitles = questionTitles,
                        options = options,
                        answers = answers,
                        creatorName = user.value.name,
                        timeLimit = timer!!)
                )
            }
            _finish.emit(Unit)
        }
    }

    override fun getCurrentUser() {
        val firebaseUser = authService.getCurrentUser()
        Log.d("debugging", firebaseUser?.uid.toString())
        firebaseUser?.let {
            viewModelScope.launch(Dispatchers.IO) {
                errorHandler { userRepo.getUser(it.uid) }?.let {  user ->
                    Log.d("debugging", user.toString())
                    _user.value = user
                }
            }
        }
    }

    override fun readCsv(lines: List<String>) {
        viewModelScope.launch {
            try {
                lines.map { line ->
                    val title = line.split(",")
                    Question(
                        question = title[0],
                        option1 = title[1],
                        option2 = title[2],
                        option3 = title[3],
                        option4 = title[4],
                        correctAnswer = title[5]
                    )
                }.toList().let {
                    Log.d("debugging", "Questions: $it")
                    if (it.all { true }) {
                        _quizQuestion.emit(it)
                        _success.emit("CSV Import Successful")
                        Log.d("debugging", "CSV Import Successful: ${it.size} questions imported.")
                    } else {
                        Log.e("debugging", "CSV Import Failed: Null values found in QuizQuestions.")
                    }
                }
            } catch (e: Exception) {
                Log.e("debugging", "Error parsing CSV file: ${e.message}")
            }
        }
    }

}