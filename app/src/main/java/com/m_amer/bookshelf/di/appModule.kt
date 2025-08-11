package com.m_amer.bookshelf.di

import com.m_amer.bookshelf.booksApiKtor.BooksApiKtor
import com.m_amer.bookshelf.repository.BookRepository
import com.m_amer.bookshelf.repository.ResourceConverter
import com.m_amer.bookshelf.screens.searsh.SearchBookViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin module for dependency injection.
 * Defines the dependencies for the application, including:
 * - HttpClient: For making network requests.
 * - BooksApiKtor: API client for fetching book data.
 * - ResourceConverter: For converting API responses to domain models.
 * - BookRepository: For managing book data.
 * - SearchBookViewModel: ViewModel for the search book screen.
 */
val appModule = module {
    single {
        HttpClient(OkHttp) {
            install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
        }
    }

    single { BooksApiKtor(get()) }
    single { ResourceConverter() }
    single { BookRepository(get(), get()) }

    viewModel { SearchBookViewModel(get()) }
}