package com.example.jointhecause.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jointhecause.ui.screens.customFontFamily
import java.util.Calendar

@Composable
fun MainScreen() {
    val searchHistory = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        ModernSearchBar { newSearch ->
            if (newSearch.isNotBlank() && newSearch !in searchHistory) {
                searchHistory.add(0, newSearch)
            }
        }
        RecentSearchCard(searchHistory)
        BottomNavBar()
    }
}

// Top Search Bar
@Composable
fun ModernSearchBar(onSearch: (String) -> Unit) {
    var location by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("Today") }
    var bags by remember { mutableStateOf("2 bags") }
    val context = LocalContext.current

    fun showDatePicker(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val selectedDate = "$dayOfMonth/${month + 1}/$year"
                onDateSelected(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            //  Search Icon
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = Color.Gray)

            Spacer(modifier = Modifier.width(0.dp))
            // Search TextField
            TextField(
                value = location,
                onValueChange = { location = it },
                placeholder = { Text("Location", fontFamily = customFontFamily) },
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .padding(0.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Date Picker
            IconButton(onClick = { showDatePicker { date = it } }) {
                Icon(imageVector = Icons.Default.DateRange, contentDescription = "Pick Date")
            }
            Text(text = date, style = MaterialTheme.typography.bodyMedium, fontFamily = customFontFamily, color = Color.DarkGray)

            Spacer(modifier = Modifier.width(8.dp))

            // Bags Icon + Text
            IconButton(onClick = { /* Select Bags */ }) {
                Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "Bags")
            }
            Text(text = bags,
                fontWeight = FontWeight.Light,
                fontFamily = customFontFamily,
                color = Color.DarkGray
            )
        }
    }
}

// Recent Searches Card
@Composable
fun RecentSearchCard(searchHistory: List<String>) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        border = BorderStroke(2.dp, Color(0xFFB0B0B0)),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Recent Searches",
                    tint = Color(0xFF6A5ACD),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Recent Searches",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4A4A4A),
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
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                        if (index < searchHistory.lastIndex) {
                            Divider(color = Color(0xFFB0B0B0))
                        }
                    }
                }
            }
        }
    }
}

// Bottom Navigation Bar
data class NavigationItem(val label: String, val icon: ImageVector)

@Composable
fun BottomNavBar() {
    var selectedTab by remember { mutableStateOf(0) }

    NavigationBar(containerColor = Color.White) {
        val items = listOf(
            NavigationItem("Browse", Icons.Default.Search),
            NavigationItem("Nearby", Icons.Default.Place),
            NavigationItem("Bookings", Icons.Default.DateRange),
            NavigationItem("Menu", Icons.Default.MoreVert)
        )

        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedTab == index,
                onClick = { selectedTab = index },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (selectedTab == index) Color(0xFFFF6600) else Color.Gray
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        color = if (selectedTab == index) Color(0xFFFF6600) else Color.Gray,
                        fontFamily = customFontFamily

                    )
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    MainScreen()
}
