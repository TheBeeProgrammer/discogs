package com.clara.clarachallenge.ui.components.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.clara.clarachallenge.R
import com.clara.domain.model.InternalServerErrorException
import com.clara.domain.model.NetworkUnavailableException

/**
 * Displays an error message with retry option.
 *
 * @param modifier Modifier for customizing layout appearance.
 * @param message Error message to display.
 * @param throwable Optional [Throwable] to map to a user-friendly error message.
 * @param onRetry Callback invoked when retrying after an error.
 */
@Composable
fun ErrorView(
    modifier: Modifier = Modifier,
    message: String = "",
    throwable: Throwable? = null,
    onRetry: () -> Unit
) {
    val errorMessage = mapErrorToMessage(throwable = throwable ?: Throwable())
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = if (throwable != null) errorMessage else message, color = Color.Red)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onRetry) {
            Text(stringResource(R.string.retry))
        }
    }
}

/**
 * Maps a [Throwable] to a user-friendly error message.
 *
 * This function checks the type of the throwable and returns a specific
 * string resource for known exceptions like [NetworkUnavailableException]
 * and [InternalServerErrorException]. For other exceptions, it returns
 * a generic "unknown error" message.
 *
 * @param throwable The exception to map to a message.
 * @return A string representing the error message.
 */
@Composable
private fun mapErrorToMessage(throwable: Throwable): String {
    return when (throwable) {
        is NetworkUnavailableException -> stringResource(R.string.error_no_internet)
        is InternalServerErrorException -> stringResource(R.string.error_unauthorized)
        else -> stringResource(R.string.unknown_error)
    }
}
