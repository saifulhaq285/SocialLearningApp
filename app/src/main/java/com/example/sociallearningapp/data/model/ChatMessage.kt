package com.example.sociallearningapp.data.model
// data/model/ChatMessage.kt

data class ChatMessage(
    val id: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val text: String = "",
    val imageUrl: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

