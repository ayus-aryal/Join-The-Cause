package com.example.jointhecause.screens

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.jointhecause.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class FillYourDetails : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController() // Create NavController
            FillYourDetailsScreen(navController)
        }
    }
}



@Composable
fun FillYourDetailsScreen(navController: NavController) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    val context = LocalContext.current
    val firestore = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.uid
    val userEmail = auth.currentUser?.email

    val isFormValid by remember {
        derivedStateOf {
            firstName.isNotBlank() &&
                    lastName.isNotBlank() &&
                    phoneNumber.length == 10 && phoneNumber.all { it.isDigit() }
        }
    }

    val customFontFamily = FontFamily(
        Font(R.font.inter) // Replace 'inter' with the name of your font file (without extension)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            "Complete your profile",
            fontSize = 32.sp,
            fontFamily = customFontFamily,
            modifier = Modifier.padding(bottom = 16.dp, top = 45.dp),
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.height(15.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "First name",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Light,
                fontSize = 18.sp,
                fontFamily = customFontFamily,
                modifier = Modifier.padding(start = 16.dp).align(Alignment.Start)
            )

            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
            )

            Spacer(modifier = Modifier.height(30.dp))
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Last name",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Light,
                fontSize = 18.sp,
                fontFamily = customFontFamily,
                modifier = Modifier.padding(start = 16.dp).align(Alignment.Start)
            )

            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Phone Number",
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = customFontFamily,
                fontWeight = FontWeight.Light,
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 16.dp).align(Alignment.Start)
            )

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    navController.navigate("home") {

                    }
                }
            ) {
                Text("Skip for now", fontFamily = customFontFamily)
            }

            Button(
                onClick = {
                    if (userId != null && userEmail != null) {
                        val userMap = hashMapOf(
                            "firstName" to firstName,
                            "lastName" to lastName,
                            "email" to userEmail,
                            "phoneNumber" to phoneNumber
                        )
                        firestore.collection("users")
                            .document(userId)
                            .set(userMap)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Profile saved successfully!", Toast.LENGTH_SHORT).show()
                                navController.navigate("home") // Navigate after success

                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "Failed to save profile.", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(context, "User not authenticated.", Toast.LENGTH_SHORT).show()
                    }
                },
                enabled = isFormValid
            ) {
                Text("Save", fontFamily = customFontFamily)
            }
                }
        }
    }


@Preview(showBackground = true)
@Composable
fun FillYourDetailsScreenPreview() {
    val navController = rememberNavController()
    FillYourDetailsScreen(navController)
}
