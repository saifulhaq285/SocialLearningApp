package com.example.sociallearningapp.ui.chat

data class Message(
    val id: String = "",          // Firebase auto-generated key
    val senderId: String = "",    // UID of sender
    val receiverId: String = "",  // UID of receiver
    val text: String = "",        // Message text
    val timestamp: Long = 0L,
    val status: String = "sent"
)
