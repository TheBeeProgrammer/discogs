package com.clara.clarachallenge.ui.components.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.clara.clarachallenge.R

/**
 * A Composable function that displays a row item with an image, title, and optional subtitle.
 *
 * @param modifier The modifier to be applied to the row item.
 * @param imageUrl The URL of the image to be displayed.
 * @param contentDescription The content description for the image.
 * @param title The title text to be displayed.
 * @param subtitle An optional subtitle text to be displayed.
 * @param imageSize The size of the image in dp. Defaults to 48.
 * @param showDivider A boolean indicating whether to show a divider below the item. Defaults to true.
 * @param onClick An optional lambda function to be executed when the item is clicked.
 */
@Composable
fun ImageTextRowItem(
    modifier: Modifier = Modifier,
    imageUrl: String,
    contentDescription: String,
    title: String,
    subtitle: String? = null,
    imageSize: Int = 48,
    showDivider: Boolean = true,
    onClick: (() -> Unit)? = null,
) {
    val error = stringResource(R.string.unknown_error)
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .then(if (onClick != null) Modifier.debouncedClickable { onClick() } else Modifier)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CachedImage(
                imageUrl = imageUrl,
                contentDescription = contentDescription,
                modifier = Modifier.size(imageSize.dp),
                placeholder = painterResource(R.drawable.ic_loading),
                error = painterResource(R.drawable.ic_error)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    text = title.ifBlank { error },
                    style = MaterialTheme.typography.titleMedium
                )
                subtitle?.let {
                    Text(
                        text = it.ifBlank { error },
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
        if (showDivider) Divider(color = Color.LightGray, paddingStart = 16.dp, paddingEnd = 16.dp)
    }
}
