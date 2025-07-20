package com.clara.clarachallenge.ui.components.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.clara.clarachallenge.R
import com.clara.domain.model.InternalServerErrorException
import com.clara.domain.model.NetworkUnavailableException

@Composable
fun mapErrorToMessage(throwable: Throwable): String {
    return when (throwable) {
        is NetworkUnavailableException -> stringResource(R.string.error_no_internet)
        is InternalServerErrorException -> stringResource(R.string.error_unauthorized)
        else -> throwable.message ?: stringResource(R.string.unknown_error)
    }
}
