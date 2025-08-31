package com.example.sociallearningapp.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.DateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    receiverId: String,
    vm: ChatViewModel = viewModel()
) {
    val messages by vm.messages.collectAsState()
    var text by remember { mutableStateOf("") }
    val currentUid = FirebaseAuth.getInstance().currentUser?.uid

    var receiverName by remember { mutableStateOf("User") }
    var receiverOnline by remember { mutableStateOf(false) }

    // 🔹 Fetch receiver's name and online status from Firebase
    LaunchedEffect(receiverId) {
        val ref = FirebaseDatabase.getInstance().getReference("users").child(receiverId)
        ref.get().addOnSuccessListener { snap ->
            receiverName = snap.child("name").value?.toString() ?: "User"
            receiverOnline = snap.child("online").value == true
        }
        vm.loadMessages(receiverId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            receiverName,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                        Text(
                            if (receiverOnline) "Online" else "Offline",
                            style = MaterialTheme.typography.labelMedium,
                            color = if (receiverOnline) Color.Green else Color.Gray
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1e3c72)
                )
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Type a message...") }
                )
                IconButton(
                    onClick = {
                        if (text.isNotBlank()) {
                            vm.sendMessage(receiverId, text.trim())
                            text = ""
                        }
                    }
                ) {
                    Icon(Icons.Default.Send, contentDescription = "Send")
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF1e3c72), Color(0xFF2a5298))
                    )
                ),
            verticalArrangement = Arrangement.Bottom
        ) {
            items(messages) { msg ->
                val isMe = msg.senderId == currentUid
                val time = remember(msg.timestamp) {
                    DateFormat.getTimeInstance(DateFormat.SHORT)
                        .format(Date(msg.timestamp))
                }

                val bubbleColor = if (isMe) {
                    when (msg.status) {
                        "sent" -> MaterialTheme.colorScheme.primary
                        "delivered" -> MaterialTheme.colorScheme.primary.copy(alpha = 0.85f)
                        "seen" -> MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                        else -> MaterialTheme.colorScheme.primary
                    }
                } else MaterialTheme.colorScheme.secondary

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
                ) {
                    Surface(
                        color = bubbleColor,
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Column(Modifier.padding(8.dp)) {
                            Text(
                                text = msg.text,
                                color = if (isMe) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.onSecondary
                            )
                            Spacer(Modifier.height(4.dp))
                            val metaColor = if (isMe) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f)
                            else MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.85f)


                            val statusText = if (isMe) {
                                when (msg.status) {
                                    "sent", "delivered" -> "✓"
                                    "seen" -> "✓✓"
                                    else -> "✓"
                                }
                            } else ""

                            Text(
                                text = if (isMe) "$time $statusText" else time,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (msg.status == "seen" && isMe) Color.Blue else metaColor
                            )
                        }
                    }
                }
            }
        }
    }
}