package com.example.sociallearningapp.ui.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class Question(
    val text: String,
    val options: List<String>,
    val answer: Int
)

class QuizViewModel : ViewModel() {
    private val questions = listOf(
        Question("Capital of Pakistan?", listOf("Karachi", "Islamabad", "Lahore", "Quetta"), 1),
        Question("2 + 2 = ?", listOf("3", "4", "5", "6"), 1),
        Question("Android is developed by?", listOf("Apple", "Microsoft", "Google", "Meta"), 2),
        Question("Jetpack Compose is for?", listOf("iOS", "Android", "Windows", "Web"), 1),
        Question("Firebase is a?", listOf("Database", "Cloud service", "IDE", "Game Engine"), 1)
    )

    private val _index = MutableStateFlow(0)
    val index = _index.asStateFlow()

    private val _score = MutableStateFlow(0)
    val score = _score.asStateFlow()

    private val _timeLeft = MutableStateFlow(10)
    val timeLeft = _timeLeft.asStateFlow()

    private var timerJob: Job? = null

    val currentQuestion: Question
        get() = questions[_index.value]

    fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            for (t in 10 downTo 1) {
                _timeLeft.value = t
                delay(1000)
            }
            nextQuestion()
        }
    }

    fun answer(selected: Int) {
        if (selected == currentQuestion.answer) {
            _score.value += 1
        }
        nextQuestion()
    }

    fun nextQuestion() {
        timerJob?.cancel()
        if (_index.value < questions.lastIndex) {
            _index.value += 1
            startTimer()
        } else {
            _index.value = -1 // mark as finished
        }
    }
}
