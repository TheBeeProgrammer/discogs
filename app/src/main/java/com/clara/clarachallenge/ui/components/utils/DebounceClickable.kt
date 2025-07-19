package com.clara.clarachallenge.ui.components.utils

import androidx.compose.foundation.clickable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private var lastClickTime = 0L

fun Modifier.debouncedClickable(
    debounceTime: Long = 500L,
    onClick: suspend () -> Unit
): Modifier = composed {
    val coroutineScope = rememberCoroutineScope()
    var clickJob by remember { mutableStateOf<Job?>(null) }

    this.clickable {
        if (clickJob?.isActive == true) return@clickable

        clickJob = coroutineScope.launch {
            onClick()
            delay(debounceTime)
        }
    }
}

