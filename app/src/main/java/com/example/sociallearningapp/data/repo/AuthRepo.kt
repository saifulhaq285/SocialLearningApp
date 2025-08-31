package com.example.sociallearningapp.data.repo

// data/repo/AuthRepo.kt
import com.example.sociallearningapp.data.model.AppUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AuthRepo(
    private val auth: FirebaseAuth,
    private val db: FirebaseDatabase
) {
    fun currentUid(): String? = auth.currentUser?.uid

    fun register(name: String, email: String, pass: String, onResult: (Result<Unit>) -> Unit) {
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { t ->
            if (!t.isSuccessful) return@addOnCompleteListener onResult(Result.failure(t.exception ?: Exception()))
            val uid = auth.currentUser?.uid ?: return@addOnCompleteListener
            val user = AppUser(uid, name, email, "")
            db.getReference("users").child(uid).setValue(user).addOnCompleteListener { w ->
                onResult(if (w.isSuccessful) Result.success(Unit) else Result.failure(w.exception ?: Exception()))
            }
        }
    }
    fun login(email: String, pass: String, onResult: (Result<Unit>) -> Unit) {
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { t ->
            onResult(if (t.isSuccessful) Result.success(Unit) else Result.failure(t.exception ?: Exception()))
        }
    }
    fun logout() = auth.signOut()
}
