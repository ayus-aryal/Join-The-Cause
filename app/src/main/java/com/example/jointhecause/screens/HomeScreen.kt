package com.example.jointhecause.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.example.jointhecause.models.Ngo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items





@Composable
fun HomeScreen() {
    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomNavBar() }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {
            SearchBar()
            FilterChips()
            NgoList()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    TopAppBar(
        title = { Text("Explore NGOs", fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Start) },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
    )
}

@Composable
fun SearchBar() {
    var text by remember { mutableStateOf("") }

    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        placeholder = { Text("Search for NGOs") },
        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search") },
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.LightGray,
            unfocusedBorderColor = Color.LightGray
        )
    )
}

@Composable
fun FilterChips() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val filters = listOf("All causes", "Health", "Education")
        filters.forEach { label ->
            AssistChip(
                onClick = { /* TODO: Implement filtering */ },
                label = { Text(label) },
                shape = RoundedCornerShape(50),
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = Color(0xFFF5F5F5),
                    labelColor = Color.Black
                )
            )
        }
    }
}

// NGO List with Firestore
@Composable
fun NgoList() {
    val firestore = FirebaseFirestore.getInstance()
    val ngoList = remember { mutableStateListOf<Ngo>() }
    var isLoading by remember { mutableStateOf(true) }

    // Listen for real-time updates
    LaunchedEffect(Unit) {
        firestore.collection("ngos").addSnapshotListener { snapshot, e ->
            if (e != null) {
                e.printStackTrace()
                return@addSnapshotListener
            }

            snapshot?.let {
                val fetchedNgos = it.documents.mapNotNull { doc ->
                    doc.toObject<Ngo>()?.copy(id = doc.id)
                }
                ngoList.clear()
                ngoList.addAll(fetchedNgos)
                isLoading = false
            }
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(ngoList) { ngo ->
                NgoCard(ngo)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}


@Composable
fun NgoCard(ngo: Ngo) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* TODO: Navigate to NGO details screen */ }
            .padding(12.dp)
    ) {
        AsyncImage(
            model = ngo.imageUrl,
            contentDescription = ngo.name,
            modifier = Modifier
                .size(50.dp)
                .background(Color.LightGray, shape = CircleShape)
                .padding(4.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(ngo.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(ngo.details, fontSize = 14.sp, color = Color.Gray)
            Text(ngo.category, fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
fun BottomNavBar() {
    var selectedTab by remember { mutableIntStateOf(0) }

    NavigationBar(containerColor = Color.White) {
        val items = listOf(
            Icons.Default.Home to "Home",
            Icons.Rounded.CalendarMonth to "Events",
            Icons.Default.Notifications to "Alerts",
            Icons.Default.Person to "Profile"
        )

        items.forEachIndexed { index, (icon, label) ->
            NavigationBarItem(
                selected = selectedTab == index,
                onClick = { selectedTab = index },
                icon = { Icon(imageVector = icon, contentDescription = label) },
                label = { Text(label) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    HomeScreen()
}
