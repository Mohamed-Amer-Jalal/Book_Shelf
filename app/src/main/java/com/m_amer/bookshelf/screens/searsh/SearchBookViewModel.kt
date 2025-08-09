package com.m_amer.bookshelf.screens.searsh

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m_amer.bookshelf.model.Book
import com.m_amer.bookshelf.model.DataOrException
import com.m_amer.bookshelf.repository.BookRepository
import kotlinx.coroutines.launch

class SearchBookViewModel(private val repository: BookRepository) : ViewModel() {
    var resultsState = mutableStateOf(DataOrException<List<Book>, Boolean, Exception>())
    var listOfBooks = mutableStateOf(listOf<Book>())

    fun searchBooks(query: String) {
        viewModelScope.launch {
            if (query.isEmpty()) return@launch
            listOfBooks.value = listOf()
            resultsState.value = repository.getBooks(query)
            resultsState.value.data?.let { listOfBooks.value = it }
        }
    }
}