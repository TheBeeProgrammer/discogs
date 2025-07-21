package com.clara.clarachallenge.ui.common

import com.clara.clarachallenge.R
import com.clara.domain.usecase.base.UseCaseResult
import com.clara.domain.usecase.base.UseCaseResult.Reason

/**
 * Converts a [UseCaseResult.Reason] to a user-friendly UI message.
 *
 * This extension function maps different reasons for a use case failure
 * to appropriate strings that can be displayed to the user.
 *
 * @return A [String] representing the UI message for the reason.
 */
fun Reason.toUiMessage(textResourceProvider: TextResourceProvider): String = when (this) {
    Reason.NoInternet -> textResourceProvider.getString(R.string.error_no_internet)
    Reason.Timeout -> textResourceProvider.getString(R.string.error_timeout)
    Reason.NotFound -> textResourceProvider.getString(R.string.error_not_found)
    is Reason.Unknown -> textResourceProvider.getString(R.string.error_unknown)
}
