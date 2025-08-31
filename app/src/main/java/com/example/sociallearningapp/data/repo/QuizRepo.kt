// data/repo/QuizRepo.kt
package com.example.sociallearningapp.data.repo
import com.example.sociallearningapp.data.model.QuizAttempt
import com.google.firebase.database.FirebaseDatabase

class QuizRepo(private val db: FirebaseDatabase) {
    fun saveAttempt(uid: String, attempt: QuizAttempt) {
        val ref = db.getReference("users").child(uid).child("quizHistory").push()
        ref.setValue(attempt.copy(id = ref.key ?: ""))
    }
}

