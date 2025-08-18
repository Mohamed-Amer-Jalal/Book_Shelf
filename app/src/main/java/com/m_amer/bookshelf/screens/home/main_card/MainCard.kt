package com.m_amer.bookshelf.screens.home.main_card

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.m_amer.bookshelf.R
import com.m_amer.bookshelf.model.Book
import com.m_amer.bookshelf.model.ImageLinks
import com.m_amer.bookshelf.navigation.BookShelfScreens
import com.m_amer.bookshelf.ui.theme.poppinsFamily

/**
 * Composable function to display the main card on the home screen.
 *
 * This card displays a welcome message and the currently reading book, if any.
 * It also provides a way to navigate to the book details screen.
 *
 * @param currentRead The currently reading book.
 * @param navController The NavController used for navigation.
 * @param readingList The list of books in the user's reading list.
 */
@Composable
fun MainCard(currentRead: Book, navController: NavController, readingList: List<Book>) {
    var loading by remember { mutableStateOf(false) }
    val hasReading = readingList.isNotEmpty()

    Card(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box {
            // خلفية الكرت
            Image(
                painter = painterResource(R.drawable.card),
                contentDescription = stringResource(R.string.card),
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text(
                    text = stringResource(R.string.track_your),
                    fontFamily = poppinsFamily,
                    fontSize = 23.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = stringResource(R.string.reading_activity),
                    fontFamily = poppinsFamily,
                    fontSize = 23.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .background(MaterialTheme.colorScheme.secondary)
                        .clickable {
                            navController.navigate("${BookShelfScreens.BookScreen.name}/${currentRead.bookID}")
                        },
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(12.dp)
                    ) {
                        if (loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(36.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            BookImage(
                                hasReading = hasReading,
                                imageUrl = currentRead.imageLinks.thumbnail,
                                onLoading = { loading = true },
                                onFinished = { loading = false }
                            )
                        }

                        if (hasReading && !loading) {
                            Column(
                                modifier = Modifier
                                    .padding(start = 12.dp)
                                    .weight(1f)
                            ) {
                                Text(
                                    text = currentRead.title,
                                    fontFamily = poppinsFamily,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = Color.White.copy(alpha = 0.85f)
                                )
                                Text(
                                    text = stringResource(R.string.continue_reading),
                                    fontFamily = poppinsFamily,
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Composable function to display the book image.
 *
 * This function displays the book image if the user has a reading list and the image URL is not null or blank.
 * Otherwise, it displays a placeholder image.
 *
 * @param hasReading A boolean indicating whether the user has a reading list.
 * @param imageUrl The URL of the book image.
 * @param onLoading A callback function to be invoked when the image starts loading.
 * @param onFinished A callback function to be invoked when the image loading is finished (either success or error).
 */
@Composable
private fun BookImage(
    hasReading: Boolean,
    imageUrl: String?,
    onLoading: () -> Unit,
    onFinished: () -> Unit
) {
    Surface(
        modifier = Modifier.size(50.dp),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.primaryContainer,
        tonalElevation = 4.dp
    ) {
        if (hasReading && !imageUrl.isNullOrBlank()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl.replace("http://", "https://"))
                    .crossfade(true).build(),
                contentDescription = stringResource(R.string.book_cover),
                contentScale = ContentScale.Crop,
                modifier = Modifier.clip(CircleShape),
                onLoading = { onLoading() },
                onSuccess = { onFinished() },
                onError = { onFinished() })
        } else {
            Image(
                painter = painterResource(id = R.drawable.emptyshelf),
                contentDescription = stringResource(R.string.empty_shelf),
                contentScale = ContentScale.Crop,
                modifier = Modifier.clip(CircleShape)
            )
        }
    }
}

@Preview
@Composable
fun MainCardPreview() {
    val context = LocalContext.current
    val navController = NavController(context)
    val readingList = listOf(Book(
        bookID = "1",
        authors = listOf("J.R.R. Tolkien"),
        averageRating = 4.9,
        categories = listOf("Fantasy", "Adventure"),
        description = "A high fantasy epic.",
        imageLinks = ImageLinks(
            smallThumbnail = "http://books.google.com/books/content?id=someId&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api",
            thumbnail = "http://books.google.com/books/content?id=someId&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api"
        ),
        language = "en",
        pageCount = 1178,
        industryIdentifiers = emptyList(),
        publishedDate = "1954",
        publisher = "Allen & Unwind",
        ratingsCount = 5000,
        subtitle = "",
        title = "The Lord of the Rings",
        searchInfo = "An epic fantasy novel."
    ))

    MainCard(
        currentRead = Book(
            bookID = "1",
            authors = listOf("J.R.R. Tolkien"),
            averageRating = 4.9,
            categories = listOf("Fantasy", "Adventure"),
            description = "A high fantasy epic.",
            imageLinks = ImageLinks(
                smallThumbnail = "http://books.google.com/books/content?id=someId&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api",
                thumbnail = "http://books.google.com/books/content?id=someId&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api"
            ),
            language = "en",
            pageCount = 1178,
            industryIdentifiers = emptyList(),
            publishedDate = "1954",
            publisher = "Allen & Unwind",
            ratingsCount = 5000,
            subtitle = "",
            title = "The Lord of the Rings",
            searchInfo = "An epic fantasy novel."
        ),
        navController = navController,
        readingList = readingList
    )
}
