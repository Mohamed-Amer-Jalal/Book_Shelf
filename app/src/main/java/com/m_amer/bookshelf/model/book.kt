package com.m_amer.bookshelf.model

import com.google.firebase.firestore.PropertyName
import java.time.LocalDate

data class Book(
    /** The unique identifier of the book */
    @PropertyName("bookID")
    val bookID: String = "",

    /** A list of authors of the book */
    val authors: List<String> = emptyList(),

    /** The average rating of the book */
    val averageRating: Double = 0.0,

    /** A list of categories the book belongs to */
    val categories: List<String> = emptyList(),

    /** A brief description of the book */
    val description: String = "",

    /** Image links related to the book */
    val imageLinks: ImageLinks = ImageLinks(),

    /** The language in which the book is written */
    val language: String = "",

    /** The number of pages the book has */
    val pageCount: Int = 0,

    /** A list of industry identifiers for the book */
    val industryIdentifiers: List<IndustryIdentifier> = emptyList(),

    /**
     * The date the book was published.
     * Stored as ISO string in Firestore, converted to LocalDate here.
     */
    val publishedDate: LocalDate = LocalDate.MIN,

    /** The publisher of the book */
    val publisher: String = "",

    /** The number of ratings the book has received */
    val ratingsCount: Int = 0,

    /** The subtitle of the book */
    val subtitle: String = "",

    /** The title of the book */
    val title: String = "",

    /** Information related to a search query for the book */
    val searchInfo: String = ""
)

data class ImageLinks(
    val thumbnail: String = "",
    val smallThumbnail: String = ""
)

data class IndustryIdentifier(
    val type: String = "",
    val identifier: String = ""
)