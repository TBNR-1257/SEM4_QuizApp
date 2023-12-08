package com.bryan.personalproject.data.model

data class Quiz(
    val id: String ="",
    val QuizId: String = "",
    val title: String = "",
    val csv: String = "",
    val correctAns: Int = 0,
    val totalQuestion: Int = 0,
    val questionTitles: List<String>,
    val options: List<String>,
    val answers: List<String>,
    val creatorName: String = "",
    val timeLimit: Long
) {
    fun toHashMap():HashMap<String, Any> {
        return hashMapOf(
            "id" to id,
            "QuizId" to QuizId,
            "title" to title,
            "csv" to csv,
            "correctAns" to correctAns,
            "totalQuestion" to totalQuestion,
            "questionTitles" to questionTitles,
            "options" to options,
            "answers" to answers,
            "creatorName" to creatorName,
            "timeLimit" to timeLimit

        )
    }

    companion object {

        fun fromHashMap(hash: Map<String, Any>): Quiz {
            return Quiz(
                id = hash["id"].toString(),
                QuizId = hash["QuizId"].toString(),
                title = hash["title"].toString(),
                csv = hash["csv"].toString(),
                questionTitles = (hash["questionTitles"] as ArrayList<*>?)?.map {
                    it.toString()
                }?.toList() ?: emptyList(),
                options = (hash["options"] as ArrayList<*>?)?.map {
                    it.toString()
                }?.toList() ?: emptyList(),
                answers = (hash["answers"] as ArrayList<*>?)?.map {
                    it.toString()
                }?.toList() ?: emptyList(),
                creatorName = hash["creatorName"].toString(),
                timeLimit = hash["timeLimit"]?.toString()?.toLong() ?: -1
            )
        }
    }
}