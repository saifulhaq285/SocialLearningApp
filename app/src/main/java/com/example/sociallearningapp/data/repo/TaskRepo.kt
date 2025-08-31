
// data/repo/TaskRepo.kt
package com.example.sociallearningapp.data.repo
import com.example.sociallearningapp.data.model.TaskItem
import com.google.firebase.database.FirebaseDatabase

class TaskRepo(private val db: FirebaseDatabase) {
    fun addTask(uid: String, item: TaskItem) {
        val ref = db.getReference("users").child(uid).child("tasks").push()
        ref.setValue(item.copy(id = ref.key ?: ""))
    }
    fun deleteTask(uid: String, id: String) =
        db.getReference("users").child(uid).child("tasks").child(id).removeValue()
    // observeTasks(...) & updateTask(...) you’ll add next
}

