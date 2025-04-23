package com.example.jointhecause.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jointhecause.models.Event
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.style.TextOverflow


@Composable
fun EventsScreen() {
    val events = remember { mutableStateListOf<Event>() }
    var isLoading by remember { mutableStateOf(true) }

    // Fetch events from Firestore
    LaunchedEffect(Unit) {
        FirebaseFirestore.getInstance().collection("events")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    e.printStackTrace()
                    return@addSnapshotListener
                }

                snapshot?.let {
                    val fetchedEvents = it.documents.mapNotNull { doc ->
                        doc.toObject<Event>()?.copy(id = doc.id)
                    }
                    events.clear()
                    events.addAll(fetchedEvents)
                    isLoading = false
                }
            }
    }

    Scaffold(
        topBar = { EventTopBar() },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color.White) // Light background for cleanliness
            ) {
                // Search Bar and Filter Chips
                SearchBar()
                FilterChips()

                // Show loading indicator or events
                if (isLoading) {
                    LoadingIndicator()
                } else {
                    EventList(events)
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventTopBar() {
    TopAppBar(
        title = { Text("Explore Events", fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Start) },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
    )
}

@Composable
fun LoadingIndicator() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = Color(0xFF00796B))
    }
}

@Composable
fun EventList(events: List<Event>) {
    LazyColumn(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        items(events) { event ->
            EventCard(event)
            Spacer(modifier = Modifier.height(16.dp)) // Add more spacing between events
        }
    }
}

@Composable
fun EventCard(event: Event) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            // Event Name
            Text(
                text = event.eventName,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Event Category
            Text(
                text = "Category: ${event.eventCategory}",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray
                ),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Event Description
            Text(
                text = event.eventDescription,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.Black
                ),
                modifier = Modifier.padding(bottom = 16.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )


        }
    }
}



@Preview(showBackground = true)
@Composable
fun PreviewEventsScreen() {
    EventsScreen()
}
