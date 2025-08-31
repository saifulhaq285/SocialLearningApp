package com.example.sociallearningapp.data.model
// data/model/TaskItem.kt

enum class Priority { LOW, MEDIUM, HIGH }
enum class Status { PENDING, IN_PROGRESS, DONE }
data class TaskItem(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val priority: Priority = Priority.MEDIUM,
    val status: Status = Status.PENDING,
    val createdAt: Long = System.currentTimeMillis()
)

