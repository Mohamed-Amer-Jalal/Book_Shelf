package com.m_amer.bookshelf.di

import com.m_amer.bookshelf.booksApiKtor.BooksApiKtor
import com.m_amer.bookshelf.repository.BookRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val appModule = module {
    single {
        HttpClient(OkHttp) {
            install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
        }
    }

    single { BooksApiKtor(get()) }

    single { BookRepository(get()) }
}