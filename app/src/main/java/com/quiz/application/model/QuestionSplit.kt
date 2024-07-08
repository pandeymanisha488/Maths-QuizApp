package com.quiz.application.model

data class QuestionSplit(
    val answer: Int,
    val first: Int,
    val operator: String,
    val questionMarkValue: String,
    val second: Int
)