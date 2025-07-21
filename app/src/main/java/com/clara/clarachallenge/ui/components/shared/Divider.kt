package com.clara.clarachallenge.ui.components.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A horizontal divider line with customizable appearance.
 *
 * @param modifier Modifier to be applied to the divider.
 * @param thickness Height/thickness of the divider line. Default is 1.dp.
 * @param color Color of the divider. Default is a low opacity onSurface color from theme.
 * @param paddingStart Start padding for the divider. Default is 0.dp.
 * @param paddingEnd End padding for the divider. Default is 0.dp.
 *
 * Example usage:
 *
 * ```kotlin
 * Divider()
 *
 * // Thicker primary color divider
 * Divider(
 *     thickness = 2.dp,
 *     color = MaterialTheme.colorScheme.primary
 * )
 *
 * // Divider with padding
 * Divider(
 *     paddingStart = 16.dp,
 *     paddingEnd = 16.dp,
 *     color = Color.Gray
 * )
 *
 * // Full-width themed divider
 * Divider(
 *     thickness = 1.dp,
 *     color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
 * )
 * ```
 */
@Composable
fun Divider(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp,
    color: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
    paddingStart: Dp = 0.dp,
    paddingEnd: Dp = 0.dp
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = paddingStart, end = paddingEnd)
            .height(thickness)
            .background(color)
    )
}
