package com.clara.clarachallenge.ui.viewmodel.fakes

import com.clara.clarachallenge.ui.common.TextResourceProvider

/**
 * A fake implementation of [TextResourceProvider] for testing purposes.
 * This class allows setting a list of resource ID and value pairs to simulate
 * string resource retrieval.
 */
class FakeTextResourceProvider : TextResourceProvider {

    var resources: List<Pair<Int, Any?>> = listOf()
    override fun getString(id: Int, vararg formatArgs: Any): String {
        return resources.find { it.first == id }?.second?.toString() ?: throw NoSuchElementException("Missing element from resources list.")
    }

    override fun getString(value: String): String {
        return value
    }
}