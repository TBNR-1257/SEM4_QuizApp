package com.bryan.personalproject.ui.screens.leaderboard

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bryan.personalproject.R
import com.bryan.personalproject.databinding.FragmentLeaderboardBinding
import com.bryan.personalproject.ui.adapter.ScoreAdapter
import com.bryan.personalproject.ui.screens.base.BaseFragment
import com.bryan.personalproject.ui.screens.leaderboard.viewModel.LeaderBoardViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class LeaderBoardFragment : BaseFragment<FragmentLeaderboardBinding>() {

    override val viewModel: LeaderBoardViewModelImpl by viewModels()
    private lateinit var adapter: ScoreAdapter
    private lateinit var categoryAdapter: ArrayAdapter<String>
    protected var categorySelect = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLeaderboardBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun setupUiComponents(view: View) {
        super.setupUiComponents(view)
        setupAdapter()

        categoryAdapter = ArrayAdapter(
            requireContext(),
            androidx.transition.R.layout.support_simple_spinner_dropdown_item,
            emptyList()
        )


        binding.run {
            autoCompleteCategory.setOnItemClickListener { _, _, position, _ ->
                val selectedQuizId = categoryAdapter.getItem(position)
                if (!selectedQuizId.isNullOrBlank()) {
                    viewModel.getScoreByQuizId(selectedQuizId)
                }
            }
        }


//        binding.run {
//            autoCompleteCategory.addTextChangedListener {
//                categorySelect = it.toString()
//                Log.d("debugging", categorySelect)
//            }
//        }
    }

    override fun setupViewModelObserver(view: View) {
        super.setupViewModelObserver(view)

        lifecycleScope.launch {
            viewModel.score.collect { results ->
                // Update the ResultAdapter with the filtered and sorted list of results
                adapter.setScore(results.sortedByDescending { it.result.toInt() })

                // Extract unique quizIds from the results
                val quizIds = results.map { it.quizId }.distinct()

//                Log.d("LeaderBoardFragment", "Unique Quiz IDs: $quizIds")

                withContext(Dispatchers.Main) {
                    // Create a new list and update the categoryAdapter
                    val newQuizIds = ArrayList<String>(quizIds)
                    categoryAdapter = ArrayAdapter(
                        requireContext(),
                        androidx.transition.R.layout.support_simple_spinner_dropdown_item,
                        newQuizIds
                    )
                    binding.autoCompleteCategory.setAdapter(categoryAdapter)
                }
            }
        }


//        lifecycleScope.launch {
//            viewModel.score.collect { results ->
//                val quizIds = results.map { it.quizId }
//                Log.d("debugging", quizIds.toString())
//                categoryAdapter = ArrayAdapter(
//                    requireContext(),
//                    androidx.transition.R.layout.support_simple_spinner_dropdown_item,
//                    quizIds
//                )
//                binding.autoCompleteCategory.setAdapter(categoryAdapter)
//                // Update the ResultAdapter with the full list of results
//                adapter.setScore(results)
//            }
//        }
    }

    private fun setupAdapter() {
        adapter = ScoreAdapter(emptyList())
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvLeaderBoard.adapter = adapter
        binding.rvLeaderBoard.layoutManager = layoutManager
    }


}