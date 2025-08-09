package com.m_amer.bookshelf.repository

import com.m_amer.bookshelf.model.Book
import com.m_amer.bookshelf.model.BooksResource
import com.m_amer.bookshelf.model.ImageLinks
import com.m_amer.bookshelf.model.IndustryIdentifier
import com.m_amer.bookshelf.model.Item

class ResourceConverter {
    fun toBookList(resource: BooksResource): List<Book> {
        return resource.items?.mapNotNull { item -> item?.let { toBook(it) } } ?: emptyList()
    }

    fun toBook(item: Item): Book {
        val v = item.volumeInfo
        return Book(
            bookID = item.id ?: "",
            authors = v?.authors?.filterNotNull() ?: emptyList(),
            averageRating = v?.averageRating ?: 0.0,
            categories = v?.categories?.filterNotNull() ?: emptyList(),
            description = v?.description ?: "",
            imageLinks = ImageLinks(
                thumbnail = v?.imageLinks?.thumbnail ?: "",
                smallThumbnail = v?.imageLinks?.smallThumbnail ?: ""
            ),
            language = v?.language ?: "",
            pageCount = v?.pageCount ?: 0,
            industryIdentifiers = v?.industryIdentifiers?.map {
                IndustryIdentifier(
                    it?.type ?: "",
                    it?.identifier ?: ""
                )
            } ?: emptyList(),
            publishedDate = v?.publishedDate ?: "",
            publisher = v?.publisher ?: "",
            ratingsCount = v?.ratingsCount ?: 0,
            subtitle = v?.subtitle ?: "",
            title = v?.title ?: "",
            searchInfo = item.searchInfo?.textSnippet ?: ""
        )
    }
}