package com.clara.clarachallenge.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.clara.clarachallenge.R
import com.clara.clarachallenge.ui.components.shared.ErrorView

@Composable
fun ValidateIntParam(
    paramName: String,
    backStackEntry: NavBackStackEntry,
    navController: NavController,
    onValid: @Composable (Int) -> Unit
) {
    val paramValue = backStackEntry.arguments?.getString(paramName)?.toIntOrNull()
    if (paramValue != null) {
        onValid(paramValue)
    } else {
        ErrorView(
            message = stringResource(id = R.string.artist_id_missing),
            onRetry = { navController.popBackStack() }
        )
    }
}
