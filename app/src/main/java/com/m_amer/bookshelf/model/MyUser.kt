package com.m_amer.bookshelf.model

import kotlinx.serialization.Serializable

/**
 * Represents a user in the application.
 *
 * This data class stores user-specific information. Default values are provided
 * for all properties to ensure safe deserialization and eliminate the need for
 * a no-argument constructor, which is beneficial for Firebase and other serialization frameworks.
 *
 * @property displayName The user's display name. Defaults to an empty string.
 * @property userID The unique identifier for the user. Defaults to an empty string.
 * @property avatar An optional URL string pointing to the user's avatar image. Defaults to null.
 * @property shelves A list of [Shelf] objects associated with the user. Defaults to an empty list.
 * @property searchHistory A list of book IDs representing the user's search history. Defaults to an empty list.
 * @property reviews A list of [Review] objects written by the user. Defaults to an empty list.
 * @property favourites A list of book IDs that the user has marked as favourites. Defaults to an empty list.
 */
@Serializable
data class MyUser(
    val displayName: String = "",
    val userID: String = "",
    val avatar: String? = null,
    val shelves: List<Shelf> = emptyList(),
    val searchHistory: List<String> = emptyList(),
    val reviews: List<Review> = emptyList(),
    val favourites: List<String> = emptyList()
)

/**
 * Represents a shelf in the user's bookshelf.
 *
 * This data class stores the name of a shelf and a list of book IDs associated with it.
 * Storing only book IDs instead of entire `Book` objects enhances efficiency, particularly
 * when dealing with large collections or when serializing data for storage or network transfer.
 * Default values are provided for properties to ensure safe deserialization.
 *
 * @property name The name of the shelf. Defaults to an empty string.
 * @property bookIds A list of unique identifiers (strings) for the books contained within this shelf.
 *                   Defaults to an empty list.
 */
@Serializable
data class Shelf(val name: String = "", val bookIds: List<String> = emptyList())

/**
 * Represents a user's review for a book.
 *
 * This class is designed to be lightweight by storing only the `bookId` instead of the entire `Book` object,
 * which helps in reducing data size, especially when dealing with lists of reviews.
 *
 * It is serializable, allowing it to be easily converted to and from formats like JSON.
 *
 * @property bookId The unique identifier of the book being reviewed. Defaults to an empty string.
 * @property rating The numerical rating given to the book, typically on a scale (e.g., 1-5). Defaults to 0.0.
 * @property reviewText The textual content of the review. Defaults to an empty string.
 */
@Serializable
data class Review(val bookId: String = "", val rating: Double = 0.0, val reviewText: String = "")