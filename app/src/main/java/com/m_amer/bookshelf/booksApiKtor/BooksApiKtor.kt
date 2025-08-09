package com.m_amer.bookshelf.booksApiKtor

import com.m_amer.bookshelf.model.BooksResource
import com.m_amer.bookshelf.model.Item
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class BooksApiKtor(private val client: HttpClient) {
    private val baseUrl = "https://www.googleapis.com/books/v1/"

    suspend fun getAllBooks(query: String): BooksResource {
        return client.get("${baseUrl}volumes") { parameter("q", query) }.body()
    }

    suspend fun getBookInfo(bookId: String): Item {
        return client.get("${baseUrl}volumes/$bookId").body()
    }
}