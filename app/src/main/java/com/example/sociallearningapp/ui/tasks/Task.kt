package com.example.sociallearningapp.ui.tasks

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.type.Date

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val dueDate: String,
    val status: TaskStatus = TaskStatus.Pending,
    val priority: TaskPriority = TaskPriority.Medium
)

enum class TaskStatus { Pending, InProgress, Completed }
enum class TaskPriority { Low, Medium, High }