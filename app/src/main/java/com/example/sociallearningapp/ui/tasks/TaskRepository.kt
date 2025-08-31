package com.example.sociallearningapp.ui.tasks

class TaskRepository(private val dao: TaskDao) {
    fun getTasks() = dao.getTasks()
    suspend fun insert(task: Task) = dao.insert(task)
    suspend fun update(task: Task) = dao.update(task)
    suspend fun delete(task: Task) = dao.delete(task)
}
