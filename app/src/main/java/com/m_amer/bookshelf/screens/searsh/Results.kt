package com.m_amer.bookshelf.screens.searsh

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import coil3.request.placeholder
import com.m_amer.bookshelf.R
import com.m_amer.bookshelf.navigation.BookShelfScreens
import com.m_amer.bookshelf.ui.theme.Yellow

/**
 * A Composable function that displays the search results for books.
 *
 * This function observes the state from the [SearchBookViewModel] to determine what to display:
 * - If [SearchBookViewModel.loading] is true, it shows a [LinearProgressIndicator].
 * - If [SearchBookViewModel.errorMessage] is not null, it displays an error message.
 * - Otherwise, it displays a [LazyColumn] of [SearchCard] composables, each representing a book
 *   from the [SearchBookViewModel.listOfBooks]. Clicking on a card navigates to the
 *   [BookShelfScreens.BookScreen] with the corresponding book ID.
 *
 * @param viewModel The [SearchBookViewModel] instance that holds the search state and results.
 * @param navController The [NavController] used for navigating to other screens.
 */
@Composable
fun Results(viewModel: SearchBookViewModel, navController: NavController) {
    val searchResults = viewModel.resultsState.value
    val listOfBooks = searchResults.data ?: emptyList()

    when {
        viewModel.loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                LinearProgressIndicator(color = Yellow)
            }
        }

        viewModel.errorMessage != null -> {
            Text(
                text = stringResource(
                    R.string.error,
                    searchResults.e?.message ?: stringResource(R.string.unknown_error)
                ),
                color = Color.Red,
                modifier = Modifier.padding(16.dp)
            )
        }

        viewModel.listOfBooks.isNotEmpty() -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                items(listOfBooks) { item ->
                    val imageUrl = item.imageLinks.thumbnail
                        .replace("^http://".toRegex(), "https://")
                        .ifEmpty { "https://media.istockphoto.com/id/1147544807/vector/thumbnail-image-vector-graphic.jpg" }

                    val title = item.title.ifEmpty { "Title information unavailable" }
                    val author =
                        item.authors.joinToString(", ").ifEmpty { "Author names not on record" }
                    val previewText = item.searchInfo
                        .takeIf { it.isNotEmpty() }
                        ?.let {
                            HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
                        }
                        ?: "Preview information not provided"

                    SearchCard(
                        bookTitle = title,
                        bookAuthor = author,
                        previewText = previewText,
                        imageUrl = imageUrl,
                        onClick = { navController.navigate(BookShelfScreens.BookScreen.name + "/${item.bookID}") }
                    )
                }
            }
        }
    }
}

/**
 * A Composable function that displays a card for a book search result.
 *
 * This card shows the book's title, author, a short preview text, and its cover image.
 * It is clickable and triggers the provided [onClick] lambda when tapped.
 *
 * @param bookTitle The title of the book.
 * @param bookAuthor The author(s) of the book.
 * @param previewText A short snippet or description of the book.
 * @param imageUrl The URL of the book's cover image.
 * @param onClick A lambda function to be executed when the card is clicked.
 */
@Composable
private fun SearchCard(
    bookTitle: String,
    bookAuthor: String,
    previewText: String,
    imageUrl: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clickable(onClick = onClick)
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .placeholder(R.drawable.placeholder_book)
                    .error(R.drawable.error_book)
                    .build(),
                contentDescription = stringResource(R.string.book_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(6.dp))
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = bookTitle,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = bookAuthor,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onBackground.copy(
                            alpha = 0.7f
                        )
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = previewText,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}