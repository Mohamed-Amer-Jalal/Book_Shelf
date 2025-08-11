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
     * This function launches a coroutine in the ViewModel's scope to perform the search.
     * If the query is empty, the function returns immediately.
     * Otherwise, it updates the `resultsState` to indicate that a search is in progress
     * and then fetches books from the repository using the provided query. The `resultsState`
     * is updated again with the results of the search operation.
     *
     * @param query The search query string.
     */
    fun searchBooks(query: String) {
        viewModelScope.launch {
            if (query.isEmpty()) return@launch
            resultsState.value = DataOrException(loading = true)
            resultsState.value = repository.getBooks(query)
        }
    }
}