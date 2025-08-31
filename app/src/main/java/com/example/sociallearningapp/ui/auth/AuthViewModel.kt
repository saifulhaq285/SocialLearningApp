package com.example.sociallearningapp.ui.auth

import androidx.lifecycle.ViewModel
import com.example.sociallearningapp.ui.chat.ChatUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class AuthUiState(val loading: Boolean = false, val error: String? = null)

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance().getReference("users")

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    fun login(email: String, pass: String, onSuccess: () -> Unit) {
        _uiState.value = AuthUiState(loading = true)
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                saveFcmToken() // ✅ Save token after login
                _uiState.value = AuthUiState()
                onSuccess()
            } else {
                _uiState.value = AuthUiState(error = task.exception?.message)
            }
        }
    }

    fun register(name: String, email: String, pass: String, onSuccess: () -> Unit) {
        _uiState.value = AuthUiState(loading = true)
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val uid = auth.currentUser!!.uid
                val user = ChatUser( // ✅ include token field (initially empty)
                    uid = uid,
                    name = name,
                    email = email,
                    profileUrl = "",
                    fcmToken = ""
                )
                db.child(uid).setValue(user).addOnSuccessListener {
                    saveFcmToken() // ✅ Save token after registration
                }
                _uiState.value = AuthUiState()
                onSuccess()
            } else {
                _uiState.value = AuthUiState(error = task.exception?.message)
            }
        }
    }

    fun logout() {
        auth.signOut()
    }

    // ✅ Fetch & store FCM token
    private fun saveFcmToken() {
        val uid = auth.currentUser?.uid ?: return
        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
            db.child(uid).child("fcmToken").setValue(token)
        }
    }
}