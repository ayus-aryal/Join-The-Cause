package com.example.jointhecause.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jointhecause.R
import com.example.jointhecause.ui.theme.AppColor

@Composable
fun WelcomeScreen(onGoogleSignInClick: () -> Unit) {
    Box(

        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 32.dp) //Optional spacing from bottom  (0xFFF8F2)
            .background(Color(0x33F9CB43))

    ) {
        // Logo at the top center
        Image(
            painter = painterResource(id = R.drawable.logo), // Replace with actual logo resource
            contentDescription = "App Logo",
            modifier = Modifier
                .size(350.dp) // Adjust size as needed
                .align(Alignment.Center)
                .padding(top = 48.dp) // Adjust spacing from top
        )

        // Google Sign-In Button & Terms Text
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedButton(
                onClick = { onGoogleSignInClick() },
                modifier = Modifier
                    .fillMaxWidth(0.8f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.google),
                    contentDescription = null,
                    modifier = Modifier.size(36.dp)
                )
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = "Continue with Google",
                    color = Color.Black
                )
            }

            // Terms & Privacy Policy Text
            Text(
                text = "By signing in, you agree to our Terms of Service and Privacy Policy.",
                fontSize = 12.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 30.dp) // Space between button and text
                    .fillMaxWidth(0.9f)
            )
        }
    }
}



