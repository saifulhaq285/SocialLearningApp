package com.example.sociallearningapp.ui.chat

data class ChatUser(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val profileUrl: String = "",
    val fcmToken: String = ""
)
