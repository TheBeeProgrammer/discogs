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
import com.clara.clarachallenge.ui.common.Screen
import com.clara.clarachallenge.ui.components.screens.artistdetail.ArtistDetailScreen
import com.clara.clarachallenge.ui.components.screens.release.ReleaseListScreen
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
                        startDestination = Screen.Search.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.Search.route) {
                            SearchScreen(navController = navController)
                        }

                        composable(Screen.ArtistDetail.route) { backStackEntry ->
                            backStackEntry.arguments?.getString("artistId")?.toIntOrNull()
                                ?.let { artistId ->
                                    ArtistDetailScreen(
                                        artistId = artistId.toString(),
                                        navController = navController,
                                        onBack = { navController.popBackStack() }
                                    )
                                }
                        }

                        composable(Screen.Releases.route) { backStackEntry ->
                            backStackEntry.arguments?.getString("artistId")?.toIntOrNull()
                                ?.let { artistId ->
                                    ReleaseListScreen(
                                        artistId = artistId,
                                        navController = navController,
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
