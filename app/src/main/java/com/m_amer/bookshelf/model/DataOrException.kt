package com.m_amer.bookshelf.model

import kotlinx.serialization.Serializable

/**
 * A generic data class that holds data, loading status, and an optional exception.
 *
 * This class is designed to represent the state of an operation that might fetch data,
 * encounter an error, or be in a loading state.
 *
 * @param T The type of the data.
 * @param U The type of the loading status.
 * @param E The type of the exception. Must be a subclass of [Exception].
 * @property data The actual data, which can be null if the operation is loading or has failed.
 * @property loading The loading status, which can be of any type and null if the operation is not loading.
 * @property e An exception object, which can be null if the operation was successful or is still loading.
 */
@Serializable
data class DataOrException<T, U, E : Exception>(
    var data: T? = null,
    var loading: U? = null,
    var e: E? = null
)