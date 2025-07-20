package com.clara.clarachallenge.ui.components.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * A composable function that displays a linear loading indicator.
 * The indicator spans the full width of its container and has vertical padding.
 */
@Composable
fun LinearLoadingView() {
    LinearProgressIndicator(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )
}

/**
 * Displays a circular loading indicator centered on the screen.
 *
 * This composable function is used to indicate that an operation is in progress.
 * It fills the entire screen and places a `CircularProgressIndicator` at its center.
 */
@Composable
fun CircularLoadingView() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}