package com.kine.extensions

/**
 * Returns the first element matching the given [predicate], or `null` if element was not found.
 */
public inline fun <T,F> Iterable<T>.firstResultOrNull(predicate: (T) -> F?): F? {
    for (element in this) {
         (predicate(element))?.apply {
            return this
        }
    }
    return null
}