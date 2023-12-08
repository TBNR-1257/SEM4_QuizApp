package com.bryan.personalproject.ui.screens.quiz

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bryan.personalproject.R
import com.bryan.personalproject.data.model.Quiz
import com.bryan.personalproject.databinding.FragmentQuizBinding
import com.bryan.personalproject.ui.screens.base.BaseFragment
import com.bryan.personalproject.ui.screens.quiz.viewModel.QuizViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class QuizFragment : BaseFragment<FragmentQuizBinding>() {
    private val args: QuizFragmentArgs by navArgs()
    override val viewModel: QuizViewModelImpl by viewModels()
    private var titleNum = 0
    private var optionNum1 = 0
    private var optionNum2 = 1
    private var optionNum3 = 2
    private var optionNum4 = 3
    private var answerNum = 0

    private var result = 0
    private var selectedAns = ""
    private var correctAns = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUiComponents(view: View) {
        super.setupUiComponents(view)
        viewModel.getQuiz(args.quizId)

        binding.btnFinish.setOnClickListener {
            val action = QuizFragmentDirections.quizToDash()
            navController.navigate(action)
        }

        binding.btnNext.setOnClickListener {
            when (binding.rgOptions.checkedRadioButtonId) {
                R.id.option1 -> selectedAns = binding.option1.text.toString()
                R.id.option2 -> selectedAns = binding.option2.text.toString()
                R.id.option3 -> selectedAns = binding.option3.text.toString()
                R.id.option4 -> selectedAns = binding.option4.text.toString()
            }

            titleNum += 1
            optionNum1 += 4
            optionNum2 += 4
            optionNum3 += 4
            optionNum4 += 4
            answerNum += 1

            if(selectedAns == correctAns) {
                result += 1
            }


            lifecycleScope.launch {
                viewModel.quiz.collect {
                    val greetingText = "You Scored: "
                    val fullName = "$greetingText${result}/${it.questionTitles.size}"
                    binding.tvScore.text = fullName
                }
            }

            lifecycleScope.launch {
                viewModel.quiz.collect {
                    if(titleNum > 0){
                        if(titleNum == it.questionTitles.size){
                            binding.LLQuestion.visibility = View.GONE
                            binding.LLResult.visibility = View.VISIBLE
                            viewModel.addResult(result.toString(), it.QuizId)
                        }
                    }
                    updateQuiz(it)
                }
            }


            lifecycleScope.launch {
                viewModel.done.collect {
                    binding.LLQuestion.visibility = View.GONE
                    binding.LLResult.visibility = View.VISIBLE
                    viewModel.addResult(result.toString(), viewModel.quiz.value.QuizId)
                    updateQuiz(viewModel.quiz.value)
                }
            }


        }
    }



    override fun setupViewModelObserver(view: View) {
        super.setupViewModelObserver(view)

        lifecycleScope.launch {
            viewModel.quiz.collect { quiz ->
                updateQuiz(quiz)

                if (quiz.timeLimit > 0L) {
                    viewModel.startCountdownTimer(quiz.timeLimit)
                }

                if (quiz.timeLimit == 0L){
                    Log.d("debugging", "you are not entering the quiz")
                    val action = QuizFragmentDirections.quizToDash()
                    navController.navigate(action)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.remainingTime.collect { remainingTime ->
                binding.tvTimer.text = "Time remaining: $remainingTime"
            }
        }

    }


    private fun updateQuiz(quiz: Quiz){
        binding.run {
            val questionTitle = quiz.questionTitles.getOrNull(titleNum) ?: ""
            tvQuestion.text = questionTitle

            // Assuming that options list has at least 4 items
            option1.text = quiz.options.getOrNull(optionNum1) ?: ""
            option2.text = quiz.options.getOrNull(optionNum2) ?: ""
            option3.text = quiz.options.getOrNull(optionNum3) ?: ""
            option4.text = quiz.options.getOrNull(optionNum4) ?: ""

            correctAns = quiz.answers.getOrNull(answerNum) ?: ""
        }
    }

}

