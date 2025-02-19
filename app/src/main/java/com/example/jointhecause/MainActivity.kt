package com.example.jointhecause

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jointhecause.myviewmodels.GoogleSignInViewModel
import com.example.jointhecause.screens.FillYourDetailsScreen
import com.example.jointhecause.screens.WelcomeScreen
import com.example.jointhecause.screens.SearchScreen

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

    NavHost(navController = navController, startDestination = "welcome_screen") {

        composable(route = "welcome_screen") {
            WelcomeScreen {
                googleSignInViewModel.handleGoogleSignIn(context,navController)
            }
        }

        composable(route = "fill_your_details") {
            FillYourDetailsScreen(navController)
        }

        composable(route = "search_screen") {
            SearchScreen()
        }




    }

}