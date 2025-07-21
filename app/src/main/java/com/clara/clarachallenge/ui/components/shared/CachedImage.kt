package com.clara.clarachallenge.ui.components.shared

import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

/**
 * A Composable function that displays an image loaded from a URL.
 * The image is cached for better performance.
 *
 * @param imageUrl The URL of the image to load.
 * @param contentDescription A description of the image for accessibility.
 * @param modifier A [Modifier] to apply to the image.
 * @param placeholder An optional [Painter] to display while the image is loading.
 * @param error An optional [Painter] to display if the image fails to load.
 * @param contentScale How the image should be scaled to fit the available space.
 * @param shape The shape to clip the image to.
 * @param fadeIn Whether to fade the image in when it loads.
 */
@Composable
fun CachedImage(
    imageUrl: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    placeholder: Painter? = null,
    error: Painter? = null,
    contentScale: ContentScale = ContentScale.Crop,
    shape: Shape = CircleShape,
    fadeIn: Boolean = true
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .apply { if (fadeIn) crossfade(true) }
            .build(),
        placeholder = placeholder,
        error = error
    )

    Image(
        painter = painter,
        contentDescription = contentDescription,
        modifier = modifier.clip(shape),
        contentScale = contentScale
    )
}
