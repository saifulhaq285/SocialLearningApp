package com.example.sociallearningapp.ui.chat

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun UsersScreen(
    vm: UsersViewModel = viewModel(),
    onUserClick: (ChatUser) -> Unit
) {
    val users by vm.users.collectAsState()
    val context = LocalContext.current

    // ✅ Initialize AdMob
    LaunchedEffect(Unit) {
        MobileAds.initialize(context) {}
        vm.loadUsers()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF673AB7), Color(0xFF512DA8)) // Purple gradient
                )
            )
            .padding(16.dp)
    ) {
        Text("Select a user to chat:", style = MaterialTheme.typography.titleMedium, color = Color.White)
        Spacer(Modifier.height(8.dp))

        users.forEach { user ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { onUserClick(user) }
            ) {
                Column(Modifier.padding(12.dp)) {
                    Text(user.name, style = MaterialTheme.typography.bodyLarge)
                    Text(user.email, style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f)) // Push ad to bottom

        // ✅ Banner Ad
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            factory = { ctx ->
                AdView(ctx).apply {
                    adUnitId = "ca-app-pub-3940256099942544/6300978111" // Test Banner Ad
                    setAdSize(com.google.android.gms.ads.AdSize.BANNER)
                    loadAd(AdRequest.Builder().build())
                }
            }
        )
    }
}
