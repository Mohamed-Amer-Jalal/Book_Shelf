package com.m_amer.bookshelf.repository

import com.m_amer.bookshelf.booksApiKtor.BooksApiKtor
import com.m_amer.bookshelf.model.Book
import com.m_amer.bookshelf.model.DataOrException

class BookRepository(private val api: BooksApiKtor) {

    suspend fun getBooks(searchQuery: String): DataOrException<List<Book>, Boolean, Exception> {
        val result = DataOrException<List<Book>, Boolean, Exception>()
        try {
            result.loading = true
            val bookResource = api.getAllBooks(searchQuery)
            result.data = ResourceConverter().toBookList(bookResource)
        } catch (e: Exception) {
            result.e = e
        } finally {
            result.loading = false
        }
        return result
    }

    suspend fun getBookInfo(bookId: String): DataOrException<Book, Boolean, Exception> {
        val result = DataOrException<Book, Boolean, Exception>()
        try {
            result.loading = true
            val book = api.getBookInfo(bookId)
            result.data = ResourceConverter().toBook(book)
        } catch (e: Exception) {
            result.e = e
        } finally {
            result.loading = false
        }
        return result
    }
}S