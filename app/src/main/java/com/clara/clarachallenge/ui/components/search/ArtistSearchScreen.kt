package com.clara.clarachallenge.ui.components.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.clara.clarachallenge.ui.components.model.ArtistUiModel
import com.clara.clarachallenge.ui.components.utils.Divider

@Composable
fun ArtistSearchScreen(
    modifier: Modifier = Modifier,
    artists: List<ArtistUiModel>,
    isLoading: Boolean,
    errorMessage: String?,
    onSearchQueryChange: (String) -> Unit,
    onArtistClick: (ArtistUiModel) -> Unit
) {
    var query by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = {
                query = it
                onSearchQueryChange(it)
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Search Artist") },
            singleLine = true
        )

        if (isLoading) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
        }

        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(artists.size) { artist ->
                ArtistItem(
                    artist = artists[artist], onClick = { onArtistClick(artists[artist]) })
                Divider(
                    thickness = 2.dp,
                    color = Color.Gray,
                    paddingStart = 16.dp
                )
            }
        }
    }
}
