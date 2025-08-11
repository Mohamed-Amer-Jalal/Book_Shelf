package com.m_amer.bookshelf.repository

import com.m_amer.bookshelf.booksApiKtor.BooksApiKtor
import com.m_amer.bookshelf.model.Book
import com.m_amer.bookshelf.model.DataOrException

/**
 * Repository class for fetching book data from the Books API.
 * This class interacts with the [BooksApiKtor] to retrieve book information
 * and uses a [ResourceConverter] to transform the API response into [Book] objects.
 *
 * @property api An instance of [BooksApiKtor] used to make network requests to the Books API.
 * @property converter An instance of [ResourceConverter] used to convert API responses into [Book] objects.
 */
class BookRepository(private val api: BooksApiKtor, private val converter: ResourceConverter) {

    /**
     * Retrieves a list of books based on a search query.
     *
     * This function makes an API call to fetch books matching the provided query.
     * It then converts the API response into a list of [Book] objects.
     *
     * @param query The search string used to find books.
     * @return A [DataOrException] object containing either a list of [Book] objects on success,
     *         or an [Exception] on failure. The boolean in DataOrException is not used here.
     */
    suspend fun getBooks(query: String): DataOrException<List<Book>, Boolean, Exception> {
        return try {
            val resource = api.getAllBooks(query)
            DataOrException(data = converter.toBookList(resource))
        } catch (e: Exception) {
            DataOrException(e = e)
        }
    }

    /**
     * Fetches detailed information for a specific book.
     *
     * This function makes an API call to retrieve information for a book identified by its ID.
     * It then converts the API response into a [Book] object.
     *
     * @param bookId The unique identifier of the book to fetch information for.
     * @return A [DataOrException] object. If the API call is successful, it contains the [Book] data.
     *         If an error occurs during the API call or data conversion, it contains the [Exception].
     */
    suspend fun getBookInfo(bookId: String): DataOrException<Book, Boolean, Exception> {
        return try {
            val item = api.getBookInfo(bookId)
            DataOrException(data = converter.toBook(item))
        } catch (e: Exception) {
            DataOrException(e = e)
        }
    }
}