package com.m_amer.bookshelf.model

import kotlinx.serialization.Serializable

/**
 * Represents a resource containing a list of books.
 *
 * @property items A list of [Item] objects, or null if no items are present.
 * @property kind The kind of resource, typically "books#volumes".
 * @property totalItems The total number of items available for this query.
 */
@Serializable
data class BooksResource(
    val items: List<Item?>? = null,
    val kind: String? = null,
    val totalItems: Int? = null
)