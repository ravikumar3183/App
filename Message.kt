package com.example.chatapp.model

data class Message(
    val id: String = "",
    val senderId: String = "",
    val friendId: String = "",
    val content: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
