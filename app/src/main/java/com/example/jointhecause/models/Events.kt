package com.example.jointhecause.models

data class Event(
    val eventName: String = "",
    val eventDescription: String = "",
    val eventCategory: String = "",
    val id: String = "" // Add this line to store Firestore document ID

)
