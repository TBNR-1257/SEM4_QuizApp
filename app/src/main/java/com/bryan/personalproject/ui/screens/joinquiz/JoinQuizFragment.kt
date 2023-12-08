package com.bryan.personalproject.ui.screens.joinquiz

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bryan.personalproject.R
import com.bryan.personalproject.databinding.FragmentJoinQuizBinding
import com.bryan.personalproject.ui.screens.base.BaseFragment
import com.bryan.personalproject.ui.screens.joinquiz.viewModel.JoinQuizViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class JoinQuizFragment : BaseFragment<FragmentJoinQuizBinding>() {

    override val viewModel: JoinQuizViewModelImpl by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentJoinQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUiComponents(view: View) {
        super.setupUiComponents(view)

        binding.run {
            btnStart.setOnClickListener {
                val id =  etQuizId.text.toString()

                viewModel.getQuiz(id)
                val action = JoinQuizFragmentDirections.joinQuizToQuiz(id)
                navController.navigate(action)
            }
        }
    }

    override fun setupViewModelObserver(view: View) {
        super.setupViewModelObserver(view)

        lifecycleScope.launch {
            viewModel.quiz.collect {
                Log.d("debugging", it.toString())
            }
        }
    }

    private fun showErrorMessage(message: String) {
        Log.d("debugging", "Error: $message")
    }
}