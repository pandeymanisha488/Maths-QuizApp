package com.quiz.application.cardSwipe

interface CardSwipeListener {
    fun onAnswerSelected(isCorrect: Boolean)
    fun onAllQuestionsAnswered()
}