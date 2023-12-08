package com.bryan.personalproject.ui.screens.leaderboard.viewModel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.bryan.personalproject.data.model.Score
import com.bryan.personalproject.data.repo.ScoreRepo
import com.bryan.personalproject.ui.screens.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaderBoardViewModelImpl @Inject constructor(
    private val scoreRepo: ScoreRepo
) : BaseViewModel(), LeaderBoardViewModel {

    private val _score: MutableStateFlow<List<Score>> = MutableStateFlow(emptyList())
    val score: StateFlow<List<Score>> = _score

    init {
        getScore()
    }

    override fun getScore() {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
                scoreRepo.getResult()
            }?.collect {
                _score.value = it
            }
        }
    }


    override fun getScoreByQuizId(quizId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
                scoreRepo.getResultByQuizId(quizId)
            }?.collect {
//                Log.d("LeaderBoardViewModelImpl", "Scores by Quiz ID: $it")
                _score.value = it
            }
        }
    }


}