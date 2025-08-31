package com.example.sociallearningapp.ui.nav
import androidx.compose.ui.unit.dp
import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.sociallearningapp.ui.chat.ChatScreen
import com.example.sociallearningapp.ui.chat.ChatUser
import com.example.sociallearningapp.ui.chat.UsersScreen
import com.example.sociallearningapp.ui.profile.ProfileScreen
import com.example.sociallearningapp.ui.quiz.QuizScreen
import com.example.sociallearningapp.ui.quiz.ScoreScreen
import com.example.sociallearningapp.ui.tasks.AddEditTaskScreen
import com.example.sociallearningapp.ui.tasks.TaskScreen
import com.example.sociallearningapp.ui.tasks.TaskViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.example.sociallearningapp.util.findActivity

object MainRoutes {
    const val QUIZ = "quiz"
    const val TASKS = "tasks"
    const val CHAT = "chat"
    const val PROFILE = "profile"
}

@Composable
fun MainNav(onLogout: () -> Unit) {
    var showWelcome by remember { mutableStateOf(true) }

    if (showWelcome) {
        WelcomeScreen { showWelcome = false }
    } else {
        MainTabsNav(onLogout = onLogout)
    }
}

@Composable
fun WelcomeScreen(onFinish: () -> Unit) {
    val pages = listOf(
        "Welcome to Social Learning App",
        "Take Quizzes and Track Progress",
        "Chat and Connect with Friends"
    )
    var pageIndex by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF1e3c72), Color(0xFF2a5298))
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = pages[pageIndex],
            style = MaterialTheme.typography.titleLarge.copy(color = Color.White),
            modifier = Modifier.padding(16.dp)
        )

        Spacer(Modifier.height(32.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (pageIndex > 0) {
                Button(onClick = { pageIndex-- }) {
                    Text("Previous")
                }
            }
            Button(onClick = {
                if (pageIndex < pages.lastIndex) pageIndex++ else onFinish()
            }) {
                Text(if (pageIndex < pages.lastIndex) "Next" else "Start")
            }
        }

        Spacer(Modifier.height(16.dp))
        Text(
            text = "${pageIndex + 1} / ${pages.size}",
            style = MaterialTheme.typography.labelMedium.copy(color = Color.White)
        )
    }
}

@Composable
fun MainTabsNav(onLogout: () -> Unit) {
    val nav = rememberNavController()
    val backStack by nav.currentBackStackEntryAsState()
    val current = backStack?.destination?.route ?: MainRoutes.QUIZ
    val context = LocalContext.current

    var interstitialAd by remember { mutableStateOf<InterstitialAd?>(null) }

    LaunchedEffect(Unit) {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            context,
            "ca-app-pub-3940256099942544/1033173712",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    interstitialAd = null
                }
            }
        )
    }

    var finished by remember { mutableStateOf(false) }
    var score by remember { mutableStateOf(0) }
    var restartKey by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = current == MainRoutes.QUIZ,
                    onClick = {
                        nav.navigate(MainRoutes.QUIZ) {
                            popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = { Icon(Icons.Default.Quiz, null) },
                    label = { Text("Quiz") }
                )
                NavigationBarItem(
                    selected = current == MainRoutes.TASKS,
                    onClick = {
                        nav.navigate(MainRoutes.TASKS) {
                            popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = { Icon(Icons.Default.ListAlt, null) },
                    label = { Text("Tasks") }
                )
                NavigationBarItem(
                    selected = current == MainRoutes.CHAT,
                    onClick = {
                        interstitialAd?.let { ad ->
                            ad.show(context.findActivity()!!)
                            interstitialAd = null
                        }
                        nav.navigate(MainRoutes.CHAT) {
                            popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = { Icon(Icons.Default.Chat, null) },
                    label = { Text("Chat") }
                )
                NavigationBarItem(
                    selected = current == MainRoutes.PROFILE,
                    onClick = {
                        nav.navigate(MainRoutes.PROFILE) {
                            popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = { Icon(Icons.Default.Person, null) },
                    label = { Text("Profile") }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = nav,
            startDestination = MainRoutes.QUIZ,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(MainRoutes.QUIZ) {
                if (!finished) {
                    QuizScreen(
                        key = restartKey,
                        onFinished = { finalScore ->
                            finished = true
                            score = finalScore
                        }
                    )
                } else {
                    ScoreScreen(score) {
                        finished = false
                        score = 0
                        restartKey++
                    }
                }
            }

            composable(MainRoutes.TASKS) {
                TaskScreen(
                    onAddTask = { nav.navigate("addTask") },
                    onEditTask = { task -> nav.navigate("editTask/${task.id}") }
                )
            }

            composable("addTask") {
                AddEditTaskScreen(onTaskSaved = { nav.popBackStack() })
            }

            composable("editTask/{taskId}") { backStack ->
                val taskId = backStack.arguments?.getString("taskId")?.toIntOrNull()
                val vm: TaskViewModel = viewModel(
                    factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
                        .getInstance(LocalContext.current.applicationContext as Application)
                )
                val task = vm.tasks.collectAsState(emptyList()).value.firstOrNull { it.id == taskId }
                AddEditTaskScreen(existingTask = task) { nav.popBackStack() }
            }

            composable(MainRoutes.CHAT) {
                UsersScreen { selectedUser: ChatUser ->
                    nav.navigate("chat/${selectedUser.uid}")
                }
            }
            composable("chat/{receiverId}") { backStack ->
                val receiverId = backStack.arguments?.getString("receiverId") ?: return@composable
                ChatScreen(receiverId = receiverId)
            }

            composable(MainRoutes.PROFILE) {
                ProfileScreen(onLogout = { onLogout() })
            }
        }
    }
}