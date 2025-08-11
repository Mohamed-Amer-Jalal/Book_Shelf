package com.m_amer.bookshelf.repository

import com.m_amer.bookshelf.model.Book
import com.m_amer.bookshelf.model.BooksResource
import com.m_amer.bookshelf.model.ImageLinks
import com.m_amer.bookshelf.model.IndustryIdentifier
import com.m_amer.bookshelf.model.Item

/**
 * Converts data from [BooksResource] and [Item] objects to [Book] objects.
 *
 * This class provides utility functions to transform the raw resource data
 * obtained from an API or other source into the application's domain model ([Book]).
 * It handles the mapping of fields and ensures that default values are used
 * for any missing or null data points, preventing runtime errors and ensuring
 * a consistent [Book] object structure.
 */
class ResourceConverter {

    /**
     * Converts a [BooksResource] object to a list of [Book] objects.
     *
     * This function takes a [BooksResource] as input, which typically represents the response
     * from an API call that returns a collection of books. It then iterates through the
     * `items` within the resource, if they exist. For each non-null item, it calls the
     * `toBook` function to convert it into a [Book] object.
     *
     * If the `items` list in the resource is null, or if all items within it are null,
     * an empty list of [Book] objects is returned.
     *
     * @param resource The [BooksResource] object to convert.
     * @return A list of [Book] objects. Returns an empty list if `resource.items` is null
     *         or contains only null items.
     */
    fun toBookList(resource: BooksResource): List<Book> =
        resource.items?.mapNotNull { it?.let(::toBook) } ?: emptyList()


    /**
     * Converts an [Item] from the Google Books API response to a [Book] object.
     *
     * This function extracts relevant information from the [Item] and its nested [Item.volumeInfo]
     * to populate the fields of a [Book] object. It handles potential null values by providing
     * default empty or zero values using Kotlin's elvis operator (`?:`) and safe calls (`?.`).
     *
     * The `volumeInfo` (aliased as `v` in the function) is the primary source for most book details.
     * Specific fields are mapped as follows:
     * - `bookID`: From `item.id`.
     * - `authors`: From `v.authors`, filtering out any null authors.
     * - `averageRating`: From `v.averageRating`.
     * - `categories`: From `v.categories`, filtering out any null categories.
     * - `description`: From `v.description`.
     * - `imageLinks`: Constructed from `v.imageLinks.thumbnail` and `v.imageLinks.smallThumbnail`.
     * - `language`: From `v.language`.
     * - `pageCount`: From `v.pageCount`.
     * - `industryIdentifiers`: Mapped from `v.industryIdentifiers`, creating [IndustryIdentifier] objects.
     * - `publishedDate`: From `v.publishedDate`.
     * - `publisher`: From `v.publisher`.
     * - `ratingsCount`: From `v.ratingsCount`.
     * - `subtitle`: From `v.subtitle`.
     * - `title`: From `v.title`.
     * - `searchInfo`: From `item.searchInfo.textSnippet`.
     *
     * If any optional field or its parent object is null, a default value (empty string, empty list, or 0/0.0)
     * is used to ensure the [Book] object is always created with non-null properties.
     *
     * @param item The [Item] object to convert.
     * @return A [Book] object populated with data from the input [Item].
     */
    fun toBook(item: Item): Book {
        val v = item.volumeInfo
        return Book(
            bookID = item.id.orEmpty(),
            authors = v?.authors?.filterNotNull() ?: emptyList(),
            averageRating = v?.averageRating ?: 0.0,
            categories = v?.categories?.filterNotNull() ?: emptyList(),
            description = v?.description.orEmpty(),
            imageLinks = ImageLinks(
                thumbnail = v?.imageLinks?.thumbnail.orEmpty(),
                smallThumbnail = v?.imageLinks?.smallThumbnail.orEmpty()
            ),
            language = v?.language.orEmpty(),
            pageCount = v?.pageCount ?: 0,
            industryIdentifiers = v?.industryIdentifiers?.map {
                IndustryIdentifier(it?.type.orEmpty(), it?.identifier.orEmpty())
            } ?: emptyList(),
            publishedDate = v?.publishedDate.orEmpty(),
            publisher = v?.publisher.orEmpty(),
            ratingsCount = v?.ratingsCount ?: 0,
            subtitle = v?.subtitle.orEmpty(),
            title = v?.title.orEmpty(),
            searchInfo = item.searchInfo?.textSnippet.orEmpty()
        )
    }
}