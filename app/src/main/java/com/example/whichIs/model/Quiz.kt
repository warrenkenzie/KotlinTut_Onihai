package com.example.whichIs.model

data class Quiz(
    val quizID:Int,
    val imageUrlAnswer: ArrayList<QuizImage>,
    val answerPosition: Int,
    val answerType: String,
    val prompt: String,
)
