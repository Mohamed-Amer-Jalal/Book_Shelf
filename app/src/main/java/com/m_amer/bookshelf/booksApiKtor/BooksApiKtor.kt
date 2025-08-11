package com.m_amer.bookshelf.booksApiKtor

import com.m_amer.bookshelf.model.BooksResource
import com.m_amer.bookshelf.model.Item
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

/**
 * A Ktor HTTP client for interacting with the Google Books API.
 *
 * This class provides methods to search for books and retrieve information about specific books.
 *
 * @property client The [HttpClient] instance used to make network requests.
 */
class BooksApiKtor(private val client: HttpClient) {
    /**
     * The base URL for the Google Books API.
     */
    private val baseUrl = "https://www.googleapis.com/books/v1/"
    /**
     * The URL for accessing book volumes.
     * This property constructs the full URL by appending "volumes" to the [baseUrl].
     */
    private val volumesUrl get() = "${baseUrl}volumes"

    /**
     * Fetches a list of books based on the provided query.
     *
     * @param query The search query to find books.
     * @return A [BooksResource] object containing the list of books matching the query.
     */
    suspend fun getAllBooks(query: String): BooksResource =
        client.get(volumesUrl) { parameter("q", query) }.body()

    /**
     * Fetches detailed information about a specific book.
     *
     * This function sends a GET request to the Google Books API to retrieve information
     * for a book identified by its unique ID.
     *
     * @param bookId The unique identifier of the book to retrieve information for.
     * @return An [Item] object containing the detailed information about the book.
     * @throws io.ktor.client.plugins.ClientRequestException if the network request fails or returns an error.
     * @throws kotlinx.serialization.SerializationException if there's an issue deserializing the API response.
     */
    suspend fun getBookInfo(bookId: String): Item = client.get("$volumesUrl/$bookId").body()
}