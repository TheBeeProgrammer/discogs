package com.clara.clarachallenge.ui.common

import com.clara.domain.usecase.model.UseCaseResult

/**
 * Converts a [UseCaseResult.Reason] to a user-friendly UI message.
 *
 * This extension function maps different reasons for a use case failure
 * to appropriate strings that can be displayed to the user.
 *
 * @return A [String] representing the UI message for the reason.
 */
fun UseCaseResult.Reason.toUiMessage(): String = when (this) {
    UseCaseResult.Reason.Unauthorized -> "You don’t have permission to access this content."
    UseCaseResult.Reason.NoInternet -> "You're offline. Please check your internet connection."
    UseCaseResult.Reason.Timeout -> "The request took too long. Please try again."
    is UseCaseResult.Reason.Unknown -> TODO("Move hardcoded error messages to string resources for localization support")
}
