package com.example.sociallearningapp.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

@Composable
fun ProfileScreen(onLogout: () -> Unit) {
    val auth = FirebaseAuth.getInstance()
    val uid = auth.currentUser?.uid
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    LaunchedEffect(uid) {
        if (uid != null) {
            val ref = FirebaseDatabase.getInstance().getReference("users").child(uid)
            ref.get().addOnSuccessListener { snap ->
                name = snap.child("name").value?.toString() ?: ""
                email = snap.child("email").value?.toString() ?: ""
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF2196F3), Color(0xFF21CBF3)) // Blue gradient
                )
            )
            .padding(24.dp)
    ) {
        Text("Profile", style = MaterialTheme.typography.headlineMedium, color = Color.White)
        Spacer(Modifier.height(16.dp))
        Text("Name: $name", color = Color.White)
        Text("Email: $email", color = Color.White)
        Spacer(Modifier.height(24.dp))
        Button(onClick = {
            auth.signOut()
            onLogout()
        }) {
            Text("Logout")
        }
    }
}