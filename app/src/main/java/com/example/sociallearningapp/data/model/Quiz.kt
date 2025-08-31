package com.example.sociallearningapp.data.model

// data/model/Quiz.kt

data class QuizQuestion(
    val id: String = "",
    val question: String = "",
    val options: List<String> = emptyList(), // size 4
    val correctIndex: Int = 0
)
data class QuizAttempt(
    val id: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val total: Int = 5,
    val correct: Int = 0
)
