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
// TODO: Convert to string resources for localization
fun UseCaseResult.Reason.toUiMessage(): String = when (this) {
    UseCaseResult.Reason.NoInternet -> "📡 No connection. Check your internet!"
    UseCaseResult.Reason.Timeout -> "⏳ Taking too long. Try again!"
    UseCaseResult.Reason.NotFound -> "🔍 Artist not found"
    is UseCaseResult.Reason.Unknown -> "⚠️ Something went wrong"
}
