package com.example.sociallearningapp.ui.tasks

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    onAddTask: () -> Unit,
    onEditTask: (Task) -> Unit = {}
) {
    val vm: TaskViewModel = viewModel(
        factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
            .getInstance(LocalContext.current.applicationContext as Application)
    )

    val tasks by vm.tasks.collectAsState(initial = emptyList())

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { onAddTask() }) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF2196F3), Color(0xFF21CBF3))
                    )
                )
                .padding(padding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)   // ✅ ensures list takes available space
                    .fillMaxWidth(),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(tasks) { task ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        elevation = CardDefaults.cardElevation()
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(task.title, style = MaterialTheme.typography.titleMedium)
                            Text("Priority: ${task.priority}")
                            Text("Status: ${task.status}")
                            Text("Due: ${task.dueDate}")

                            Spacer(Modifier.height(8.dp))

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // 🔹 Edit Button
                                IconButton(onClick = { onEditTask(task) }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Edit Task")
                                }

                                // Delete Button
                                IconButton(onClick = { vm.deleteTask(task) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete Task")
                                }
                            }
                        }
                    }
                }
            }

            // ✅ Banner Ad at bottom
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                factory = { context ->
                    AdView(context).apply {
                        setAdSize(AdSize.BANNER)
                        adUnitId = "ca-app-pub-3940256099942544/6300978111" // Google test ID
                        loadAd(AdRequest.Builder().build())
                    }
                }
            )
        }
    }
}