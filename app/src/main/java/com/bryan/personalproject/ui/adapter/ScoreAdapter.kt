package com.bryan.personalproject.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bryan.personalproject.data.model.Score
import com.bryan.personalproject.databinding.ItemLayoutLeaderboardBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ScoreAdapter(
    private var score: List<Score>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(child: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemLayoutLeaderboardBinding.inflate(
            LayoutInflater.from(child.context),
            child,
            false
        )
        return ScoreViewHolder(binding)
    }

    override fun getItemCount() = score.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val result = score[position]
        if (holder is ScoreAdapter.ScoreViewHolder) {
            holder.bind(result)
        }
    }

    fun setScore(score: List<Score>) {
        this.score = score
        notifyDataSetChanged()

    }

    inner class ScoreViewHolder(
        private val binding: ItemLayoutLeaderboardBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(score: Score) {
            binding.run {
                tvUsername.text = score.name
                tvScore.text = score.result
                tvQuizId.text = score.quizId
//                val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
//                val formattedDate = dateFormat.format(Date(score.createdAt))
//                tvFinishtime.text = formattedDate
            }
        }
    }

}