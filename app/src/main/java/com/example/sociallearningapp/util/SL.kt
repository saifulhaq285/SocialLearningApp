package com.example.sociallearningapp.util

import com.example.sociallearningapp.data.repo.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

object SL {
    val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    val db: FirebaseDatabase by lazy { FirebaseDatabase.getInstance() }
    val storage: FirebaseStorage by lazy { FirebaseStorage.getInstance() }

    val authRepo by lazy { AuthRepo(auth, db) }
    val userRepo by lazy { UserRepo(db) }
    val taskRepo by lazy { TaskRepo(db) }
    val quizRepo by lazy { QuizRepo(db) }
    val chatRepo by lazy { ChatRepo(db, storage) }
}
