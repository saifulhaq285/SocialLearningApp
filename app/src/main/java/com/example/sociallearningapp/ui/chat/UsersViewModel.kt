package com.example.sociallearningapp.ui.chat

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UsersViewModel : ViewModel() {

    private val db: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("users")
    private val auth = FirebaseAuth.getInstance()

    private val _users = MutableStateFlow<List<ChatUser>>(emptyList())
    val users: StateFlow<List<ChatUser>> = _users

    fun loadUsers() {
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val currentUid = auth.currentUser?.uid
                val list = snapshot.children.mapNotNull { it.getValue(ChatUser::class.java) }
                    .filter { it.uid != currentUid } // ✅ Exclude current user
                _users.value = list // ✅ simpler than emit inside coroutine
            }

            override fun onCancelled(error: DatabaseError) {
                // Optionally log or handle errors
            }
        })
    }
}