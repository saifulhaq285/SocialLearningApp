package com.example.sociallearningapp.ui.tasks

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(app: Application) : AndroidViewModel(app) {
    private lateinit var repo: TaskRepository

    val tasks by lazy {
        repo.getTasks().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    }

    init {
        val dao = TaskDatabase.getDatabase(app).taskDao()
        repo = TaskRepository(dao)
    }

    fun addTask(task: Task) = viewModelScope.launch { repo.insert(task) }

    // ✅ Safe enum conversion to prevent crashes
    fun addTask(
        title: String,
        description: String,
        dueDate: String,
        priority: String,
        status: String
    ) {
        viewModelScope.launch {
            try {
                val task = Task(
                    title = title,
                    description = description,
                    dueDate = dueDate,
                    priority = TaskPriority.values().firstOrNull { it.name.equals(priority, true) }
                        ?: TaskPriority.Medium,
                    status = TaskStatus.values().firstOrNull { it.name.equals(status, true) }
                        ?: TaskStatus.Pending
                )
                repo.insert(task)
            } catch (e: Exception) {
                e.printStackTrace() // log error instead of crashing
            }
        }
    }

    fun updateTask(task: Task) = viewModelScope.launch { repo.update(task) }
    fun deleteTask(task: Task) = viewModelScope.launch { repo.delete(task) }
}