package com.example.jointhecause.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jointhecause.models.Event
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject


@Composable
fun EventsScreen() {
    val events = remember { mutableStateListOf<Event>() }
    var isLoading by remember { mutableStateOf(true) }
    var selectedCategory by remember { mutableStateOf<String?>(null) } // null = All
    var searchQuery by remember { mutableStateOf("") }

    val categories = listOf("All", "Health", "Education", "Environment", "Social")

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
                    .background(Color.White)
            ) {
                // Search
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it }
                )

                // Category Filter Chips
                FilterChips(
                    categories = categories,
                    selectedCategory = selectedCategory,
                    onCategorySelected = { selected ->
                        selectedCategory = if (selected == "All") null else selected
                    }
                )

                // Event List
                if (isLoading) {
                    LoadingIndicator()
                } else {
                    val filteredEvents = events.filter { event ->
                        (selectedCategory == null || event.eventCategory == selectedCategory) &&
                                (event.eventName.contains(searchQuery, ignoreCase = true) ||
                                        event.eventDescription.contains(searchQuery, ignoreCase = true))
                    }

                    EventList(filteredEvents)
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
                    color = Color(0xFF4CAF50)
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

@Composable
fun FilterChips(
    categories: List<String>,
    selectedCategory: String?,
    onCategorySelected: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            FilterChip(
                selected = selectedCategory == category || (selectedCategory == null && category == "All"),
                onClick = { onCategorySelected(category) },
                label = { Text(category) }
            )
        }
    }
}

@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search") },
        placeholder = { Text("Search Events") },
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.LightGray,
            unfocusedBorderColor = Color.LightGray
        )
    )
}





@Preview(showBackground = true)
@Composable
fun PreviewEventsScreen() {
    EventsScreen()
}
