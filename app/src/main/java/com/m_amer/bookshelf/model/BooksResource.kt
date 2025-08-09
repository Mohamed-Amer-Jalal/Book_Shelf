package com.m_amer.bookshelf.model

import kotlinx.serialization.Serializable

@Serializable
data class BooksResource(
    val items: List<Item?>? = null,
    val kind: String? = null,
    val totalItems: Int? = null
)