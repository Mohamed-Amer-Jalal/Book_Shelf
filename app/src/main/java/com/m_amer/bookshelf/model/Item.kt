package com.m_amer.bookshelf.model

import kotlinx.serialization.Serializable

/**
 * Represents a single item in a list of books, typically returned from a book search API.
 *
 * This data class encapsulates various pieces of information about a book,
 * including access details, sales information, search-related snippets,
 * and detailed volume information.
 *
 * @property accessInfo Information about how the content of this item can be accessed.
 * @property tag A tag associated with the item, often used for categorization or identification.
 * @property id A unique identifier for this item.
 * @property kind The type of resource this item represents (e.g., "books#volume").
 * @property saleInfo Information related to the saleability of this item.
 * @property searchInfo Contains a snippet of text from the book that matches the search query.
 * @property selfLink A URL link to this specific item resource.
 * @property volumeInfo Detailed information about the book's content, metadata, and identifiers.
 */
@Serializable
data class Item(
    val accessInfo: AccessInfo?,
    val tag: String?,
    val id: String?,
    val kind: String?,
    val saleInfo: SaleInfo?,
    val searchInfo: SearchInfo?,
    val selfLink: String?,
    val volumeInfo: VolumeInfo?
)

/**
 * Data class representing access information for a book item.
 *
 * @property accessViewStatus The access view status for this volume. (e.g., "FULL_PUBLIC_DOMAIN", "SAMPLE", "NONE").
 * @property country The two-letter ISO_3166-1 country code for which this access information is valid.
 * @property embeddable Whether this volume can be embedded in a website.
 * @property epub Information about the EPUB version of this volume.
 * @property pdf Information about the PDF version of this volume.
 * @property publicDomain Whether this volume is in public domain.
 * @property quoteSharingAllowed Whether quote sharing is allowed for this volume.
 * @property textToSpeechPermission The text-to-speech permission for this volume. (e.g., "ALLOWED", "ALLOWED_FOR_ACCESSIBILITY", "NOT_ALLOWED").
 * @property viewability The read access of a volume. (e.g., "PAGES", "PARTIAL", "ALL_PAGES", "NO_PAGES", "UNKNOWN").
 * @property webReaderLink URL to read this volume on the Google Books site.
 */
@Serializable
data class AccessInfo(
    val accessViewStatus: String?,
    val country: String?,
    val embeddable: Boolean?,
    val epub: Epub?,
    val pdf: Pdf?,
    val publicDomain: Boolean?,
    val quoteSharingAllowed: Boolean?,
    val textToSpeechPermission: String?,
    val viewability: String?,
    val webReaderLink: String?
)

/**
 * Represents the EPUB format availability for a book.
 *
 * @property acsTokenLink Link to retrieve the ACS token for EPUB access, if available.
 * @property isAvailable Indicates whether the EPUB format is available for the book.
 */
@Serializable
data class Epub(val acsTokenLink: String?, val isAvailable: Boolean?)

/**
 * Represents information about the availability of the PDF version of a book.
 *
 * @property acsTokenLink A link to acquire the ACS token for accessing the PDF. This may be null if no token is available or needed.
 * @property isAvailable A boolean indicating whether the PDF version of the book is available. This may be null if the availability status is unknown.
 */
@Serializable
data class Pdf(val acsTokenLink: String?, val isAvailable: Boolean?)

/**
 * Represents sales information for a book.
 *
 * @property country The country for which this sales information is applicable.
 * @property isEbook Indicates whether the book is available as an ebook.
 * @property saleability The saleability status of the book (e.g., "FOR_SALE", "NOT_FOR_SALE").
 */
@Serializable
data class SaleInfo(val country: String?, val isEbook: Boolean?, val saleability: String?)

/**
 * Data class representing search-related information for a book item.
 *
 * @property textSnippet A snippet of text from the book that matches the search query.
 */
@Serializable
data class SearchInfo(val textSnippet: String?)

/**
 * Data class representing detailed information about a book volume.
 *
 * @property allowAnonLogging Whether anonymous logging is allowed.
 * @property authors List of authors of the book.
 * @property averageRating The average rating of the book.
 * @property canonicalVolumeLink The canonical link to this volume.
 * @property categories List of categories the book belongs to.
 * @property contentVersion The version of the content.
 * @property description A description of the book.
 * @property imageLinks Links to images related to the book (e.g., cover art).
 * @property industryIdentifiers List of industry identifiers (e.g., ISBN).
 * @property infoLink A link to more information about the book.
 * @property language The language of the book.
 * @property maturityRating The maturity rating of the book.
 * @property pageCount The number of pages in the book.
 * @property penalizationSummary Information about penalization, if applicable (e.g., for comics).
 * @property previewLink A link to a preview of the book.
 * @property printType The type of print (e.g., "BOOK", "MAGAZINE").
 * @property publishedDate The date the book was published.
 * @property publisher The publisher of the book.
 * @property ratingsCount The number of ratings the book has received.
 * @property readingModes Information about available reading modes.
 * @property subtitle The subtitle of the book.
 * @property title The title of the book.
 */
@Serializable
data class VolumeInfo(
    val allowAnonLogging: Boolean?,
    val authors: List<String?>?,
    val averageRating: Double?,
    val canonicalVolumeLink: String?,
    val categories: List<String?>?,
    val contentVersion: String?,
    val description: String?,
    val imageLinks: ImageLinks?,
    val industryIdentifiers: List<IndustryIdentifier?>?,
    val infoLink: String?,
    val language: String?,
    val maturityRating: String?,
    val pageCount: Int?,
    val penalizationSummary: PenalizationSummary?,
    val previewLink: String?,
    val printType: String?,
    val publishedDate: String?,
    val publisher: String?,
    val ratingsCount: Int?,
    val readingModes: ReadingModes?,
    val subtitle: String?,
    val title: String?
)

/**
 * Represents the penalization summary for a volume.
 * This indicates whether the volume contains ePUB bubbles or image bubbles.
 *
 * @property containsEpubBubbles Indicates whether the volume contains ePUB bubbles.
 * @property containsImageBubbles Indicates whether the volume contains image bubbles.
 */
@Serializable
data class PenalizationSummary(
    val containsEpubBubbles: Boolean?,
    val containsImageBubbles: Boolean?
)

/**
 * Represents the reading modes available for a book.
 *
 * @property image Indicates whether the book is available in image mode.
 * @property text Indicates whether the book is available in text mode.
 */
@Serializable
data class ReadingModes(val image: Boolean?, val text: Boolean?)