package com.example.jointhecause.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jointhecause.R

class SearchScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SearchScreen()
        }
    }
}

// Define Custom Font
val customFontFamily = FontFamily(Font(R.font.inter))

@Composable
fun SearchScreen() {
    var location by remember { mutableStateOf(TextFieldValue("")) }
    var checkInDate by remember { mutableStateOf(TextFieldValue("")) }
    var checkOutDate by remember { mutableStateOf(TextFieldValue("")) }
    var noOfBags by remember { mutableStateOf(TextFieldValue("")) }
    var searchHistory by remember { mutableStateOf(listOf<String>()) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Main Search Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(2.dp, Color(0xFFB0B0B0)),
                modifier = Modifier.padding(8.dp),

                ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "Find Your Luggage Storage",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4A4A4A), // Bright Grey
                        fontFamily = customFontFamily
                    )

                    CustomTextField(value = location, onValueChange = { location = it }, label = "Enter Location")
                    CustomTextField(value = checkInDate, onValueChange = { checkInDate = it }, label = "Check-In Date")
                    CustomTextField(value = checkOutDate, onValueChange = { checkOutDate = it }, label = "Check-Out Date")
                    CustomTextField(value = noOfBags, onValueChange = { noOfBags = it }, label = "Number of Bags")

                    Button(
                        onClick = {
                            val searchEntry = "${location.text}, ${checkInDate.text}, ${checkOutDate.text}, ${noOfBags.text}"
                            if (searchEntry.isNotBlank()) {
                                searchHistory = listOf(searchEntry) + searchHistory.take(4)
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFDBA09)), // Fuel Yellow
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Search", fontSize = 18.sp, color = Color.White, fontFamily = customFontFamily)
                    }
                }
            }

            // Recent Searches Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(2.dp, Color(0xFFB0B0B0)),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Recent Searches",
                            tint = Color(0xFF6A5ACD), // Purple Haze
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Recent Searches",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4A4A4A), // Bright Grey
                            fontFamily = customFontFamily
                        )
                    }

                    if (searchHistory.isEmpty()) {
                        Text(
                            "No recent searches",
                            fontSize = 16.sp,
                            color = Color.Gray,
                            fontFamily = customFontFamily
                        )
                    } else {
                        searchHistory.forEachIndexed { index, searchItem ->
                            Column {
                                Text(
                                    searchItem,
                                    fontSize = 16.sp,
                                    color = Color.Gray,
                                    fontFamily = customFontFamily,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                                if (index < searchHistory.lastIndex) {
                                    Divider(color = Color(0xFFB0B0B0)) // Mid Grey Divider
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// Custom TextField
@Composable
fun CustomTextField(value: TextFieldValue, onValueChange: (TextFieldValue) -> Unit, label: String) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color(0xFF4A4A4A)) }, // Bright Grey
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
        textStyle = TextStyle(fontSize = 16.sp, fontFamily = customFontFamily, color = Color(0xFF4A4A4A)), // Bright Grey
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF58A6FF), // Day Sky Blue
            unfocusedBorderColor = Color(0xFFB0B0B0), // Mid Grey
            cursorColor = Color(0xFF58A6FF) // Cursor color
        )
    )
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    SearchScreen()
}
