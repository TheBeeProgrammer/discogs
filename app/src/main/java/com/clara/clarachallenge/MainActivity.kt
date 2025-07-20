package com.clara.clarachallenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.clara.clarachallenge.ui.components.screens.artistdetail.ArtistDetailScreen
import com.clara.clarachallenge.ui.components.screens.search.SearchScreen
import com.clara.clarachallenge.ui.theme.ClarachallengeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ClarachallengeTheme {
                val navController = rememberNavController()

                Scaffold { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "search",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("search") {
                            SearchScreen(navController = navController)
                        }
                        composable("artistDetail/{artistId}") { backStackEntry ->
                            val artistId =
                                backStackEntry.arguments?.getString("artistId")?.toIntOrNull()
                            artistId?.let {
                                ArtistDetailScreen(
                                    artistId = it,
                                    onBack = { navController.popBackStack() }
                                )
                            }
                        }
                    }
                }
            }
        }

    }
}
