package com.example.jointhecause.models


data class Ngo(
    val id: String = "",  // Unique identifier from Firestore
    val name: String = "",
    val details: String = "",
    val category: String = "",
    val imageUrl: String = "" // URL to NGO's logo or image
)

