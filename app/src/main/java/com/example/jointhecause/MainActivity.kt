package com.example.jointhecause

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.jointhecause.myviewmodels.GoogleSignInViewModel
import com.example.jointhecause.screens.BottomNavBar
import com.example.jointhecause.screens.EventsScreen
import com.example.jointhecause.screens.FillYourDetailsScreen
import com.example.jointhecause.screens.HomeScreen
import com.example.jointhecause.screens.WelcomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JoinTheCause()
        }
    }
}

@Composable
fun JoinTheCause() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val googleSignInViewModel = GoogleSignInViewModel()

    Scaffold(
        bottomBar = {
            val currentRoute = navController
                .currentBackStackEntryAsState().value?.destination?.route

            // Only show BottomNavBar on main screens
            if (currentRoute in listOf("home", "events", "alerts", "profile")) {
                BottomNavBar(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "welcome_screen",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("welcome_screen") {
                WelcomeScreen {
                    googleSignInViewModel.handleGoogleSignIn(context, navController)
                }
            }

            composable("fill_your_details") {
                FillYourDetailsScreen(navController)
            }

            composable("home") { HomeScreen() }
            composable("events") { EventsScreen() }
            // composable("alerts") { AlertsScreen() }
            // composable("profile") { ProfileScreen() }
        }
    }
}
