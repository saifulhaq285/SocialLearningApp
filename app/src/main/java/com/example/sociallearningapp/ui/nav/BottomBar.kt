package com.example.sociallearningapp.ui.nav

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School

@Composable
fun BottomBar(nav: NavController) {
    val backStack by nav.currentBackStackEntryAsState()
    val current = backStack?.destination?.route

    val items = listOf(
        Triple(Dest.Quiz, Icons.Default.School as ImageVector, "Quiz"),   // ✅ replaced Quiz with School
        Triple(Dest.Tasks, Icons.Default.List, "Tasks"),                  // ✅ replaced ListAlt with List
        Triple(Dest.Chat, Icons.Default.Chat, "Chat"),
        Triple(Dest.Profile, Icons.Default.Person, "Profile"),
    )

    NavigationBar {
        items.forEach { (dest, icon, label) ->
            NavigationBarItem(
                selected = current == dest.route,
                onClick = { if (current != dest.route) nav.navigate(dest.route) },
                icon = { Icon(icon, contentDescription = label) },
                label = { Text(label) }
            )
        }
    }
}