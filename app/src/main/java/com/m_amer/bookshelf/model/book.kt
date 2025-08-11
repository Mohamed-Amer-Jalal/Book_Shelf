package com.m_amer.bookshelf.model

import kotlinx.serialization.Serializable

/**
 * Represents a book with its details.
 *
 * @property bookID The unique identifier of the book.
 * @property authors A list of authors of the book.
 * @property averageRating The average rating of the book.
 * @property categories A list of categories the book belongs to.
 * @property description A brief description of the book.
 * @property imageLinks Image links related to the book.
 * @property language The language in which the book is written.
 * @property pageCount The number of pages the book has.
 * @property industryIdentifiers A list of industry identifiers for the book.
 * @property publishedDate The date the book was published.
 * @property publisher The publisher of the book.
 * @property ratingsCount The number of ratings the book has received.
 * @property subtitle The subtitle of the book.
 * @property title The title of the book.
 * @property searchInfo Information related to a search query for the book.
 */
@Serializable
data class Book(
    var bookID: String,
    val authors: List<String>,
    val averageRating: Double,
    val categories: List<String>,
    val description: String,
    val imageLinks: ImageLinks,
    val language: String,
    val pageCount: Int,
    val industryIdentifiers: List<IndustryIdentifier>,
    val publishedDate: String,
    val publisher: String,
    val ratingsCount: Int,
    val subtitle: String,
    val title: String,
    val searchInfo: String
) {
    constructor() : this(
        "",
        emptyList(),
        0.0,
        emptyList(),
        "",
        ImageLinks("", ""),
        "",
        0,
        emptyList(),
        "",
        "",
        0,
        "",
        "",
        ""
    )
}

/**
 * Data class representing image links for a book.
 *
 * @property thumbnail The URL of the thumbnail image for the book.
 * @property smallThumbnail The URL of the small thumbnail image for the book.
 */
@Serializable
data class ImageLinks(val thumbnail: String = "", val smallThumbnail: String = "")

/**
 * Represents an industry identifier for a book.
 *
 * @property type The type of the identifier (e.g., ISBN_10, ISBN_13).
 * @property identifier The actual identifier string.
 */
@Serializable
data class IndustryIdentifier(val type: String = "", val identifier: String = "")