package com.m_amer.bookshelf.screens.searsh

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m_amer.bookshelf.model.Book
import com.m_amer.bookshelf.model.DataOrException
import com.m_amer.bookshelf.repository.BookRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for the book search screen.
 *
 * This ViewModel is responsible for handling the business logic related to searching for books.
 * It interacts with the [BookRepository] to fetch book data and updates the UI state accordingly.
 *
 * @property repository The [BookRepository] instance used to fetch book data.
 */
class SearchBookViewModel(private val repository: BookRepository) : ViewModel() {
    /**
     * Represents the state of the book search results.
     *
     * This property holds a [DataOrException] object that encapsulates the search results,
     * loading state, and any potential errors that occurred during the search operation.
     * The UI can observe this state to display the appropriate content based on the search outcome.
     *
     * - `data`: A list of [Book] objects representing the search results if the search was successful.
     * - `loading`: A boolean indicating whether the search operation is currently in progress.
     * - `e`: An [Exception] object representing any error that occurred during the search operation.
     */
    var resultsState = mutableStateOf(DataOrException<List<Book>, Boolean, Exception>())

    /**
     * Indicates whether the book search operation is currently in progress.
     *
     * This property returns `true` if the `loading` flag in the `resultsState` is `true`,
     * indicating that a search operation is currently active. Otherwise, it returns `false`.
     * The UI can use this property to display a loading indicator while the search is in progress.
     */
    val loading: Boolean get() = resultsState.value.loading == true

    /**
     * The error message associated with the book search operation, if any.
     *
     * This property provides access to the error message from the `resultsState` if an exception occurred
     * during the book search. It returns `null` if there is no error or if the search was successful.
     * The UI can use this property to display an appropriate error message to the user.
     */
    val errorMessage: String? get() = resultsState.value.e?.message

    /**
     * A list of [Book] objects representing the search results.
     *
     * This property provides access to the list of books fetched from the repository.
     * If no books are found or if an error occurs, it returns an empty list.
     */
    val listOfBooks: List<Book> get() = resultsState.value.data ?: emptyList()

    /**
     * Searches for books based on the provided query.
     *
     * This function initiates an asynchronous search operation within the ViewModel's scope.
     *
     * - If the `query` is empty, the function returns immediately without performing a search.
     * - Otherwise, it updates `resultsState` to indicate that a search is in progress (`loading = true`).
     * - It then calls the `repository.getBooks(query)` function to fetch books matching the query.
     * - The `resultsState` is then updated with the result from the repository call, which includes
     *   the fetched data (or null if an error occurred), the loading state (which will be false upon completion
     *   by the repository), and any exception (`e`) that might have occurred during the repository call.
     * - If an exception occurs directly within this function's try block (e.g., during the repository call itself),
     *   `resultsState` is updated to reflect that the loading is complete (`loading = false`),
     *   the data is null, and the caught exception (`e`) is stored.
     *
     * @param query The string to search for in book titles, authors, etc.
     */
    fun searchBooks(query: String) {
        viewModelScope.launch {
            if (query.isEmpty()) return@launch
            resultsState.value = DataOrException(loading = true)
            try {
                val result = repository.getBooks(query)
                resultsState.value =
                    DataOrException(data = result.data, loading = false, e = result.e)
            } catch (e: Exception) {
                resultsState.value = DataOrException(data = null, loading = false, e = e)
            }
        }
    }
}