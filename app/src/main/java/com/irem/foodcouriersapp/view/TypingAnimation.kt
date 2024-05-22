package com.irem.foodcouriersapp.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun TypingAnimation(modifier: Modifier = Modifier) {
    val dots = listOf(".", "..", "...") // List of dot animations
    var dotIndex by remember { mutableStateOf(0) } // State to keep track of current dot animation

    // LaunchedEffect to create a coroutine that updates the dotIndex periodically
    LaunchedEffect(Unit) {
        while (true) {
            delay(500) // Change the delay as needed
            dotIndex = (dotIndex + 1) % dots.size // Cycle through the dots list
        }
    }

    Box(
        modifier = modifier
            .padding(8.dp), // Add padding to the Box
        contentAlignment = Alignment.Center // Center the content inside the Box
    ) {
        Text(
            text = "Typing${dots[dotIndex]}", // Display the current typing animation
            style = MaterialTheme.typography.bodyMedium, // Use the bodyMedium typography style
            color = Color.Gray // Set the text color to gray
        )
    }
}
