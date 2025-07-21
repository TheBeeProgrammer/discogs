package com.clara.clarachallenge.ui.common

import android.content.Context
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * To pass the string values without using the context inside the view model and use it in the ui
 */
interface TextResourceProvider {
    fun getString(@StringRes id: Int, vararg formatArgs: Any): String

    fun getString(value: String): String
}

@Singleton
class TextResourceProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : TextResourceProvider {

    override fun getString(value: String): String {
        return value
    }

    override fun getString(id: Int, vararg formatArgs: Any): String =
        context.getString(id, *formatArgs)
}
