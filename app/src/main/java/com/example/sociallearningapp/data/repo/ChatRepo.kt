
// data/repo/ChatRepo.kt
package com.example.sociallearningapp.data.repo
import com.example.sociallearningapp.data.model.ChatMessage
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class ChatRepo(private val db: FirebaseDatabase, private val storage: FirebaseStorage) {
    fun sendMessage(msg: ChatMessage) {
        val ref = db.getReference("chat").child("messages").push()
        ref.setValue(msg.copy(id = ref.key ?: ""))
    }
}