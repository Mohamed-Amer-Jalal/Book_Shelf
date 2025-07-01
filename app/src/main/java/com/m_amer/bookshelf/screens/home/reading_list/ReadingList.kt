package com.m_amer.bookshelf.screens.home.reading_list

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.m_amer.bookshelf.R
import com.m_amer.bookshelf.model.Book
import com.m_amer.bookshelf.model.ImageLinks
import com.m_amer.bookshelf.navigation.BookShelfScreens
import com.m_amer.bookshelf.ui.theme.Yellow
import com.m_amer.bookshelf.ui.theme.poppinsFamily
import java.time.LocalDate

@Composable
fun ReadingList(
    navController: NavController,
    loading: Boolean,
    readingList: List<Book>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        SectionTitle()

        when {
            loading -> LoadingView(Modifier.weight(1f))

            !loading && readingList.isEmpty() -> EmptyReadingListView(Modifier.weight(1f))

            else -> ReadingListContent(
                readingList = readingList,
                navController = navController,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 20.dp, vertical = 5.dp)
            )
        }
    }
}

@Composable
private fun SectionTitle() {
    Text(
        text = stringResource(R.string.my_reading_list),
        fontFamily = poppinsFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
    )
}

@Composable
private fun LoadingView(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        LinearProgressIndicator(modifier = Modifier.fillMaxWidth(0.5f), color = Yellow)
    }
}

@Composable
private fun EmptyReadingListView(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.emptyshelf),
            contentDescription = stringResource(R.string.empty_shelf),
            modifier = Modifier.padding(bottom = 10.dp)
        )
        Text(
            text = stringResource(R.string.no_current_reads),
            fontFamily = poppinsFamily,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
        )
        Text(
            text = stringResource(R.string.explore_books),
            fontFamily = poppinsFamily,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            modifier = Modifier.padding(horizontal = 40.dp, vertical = 4.dp)
        )
        Spacer(modifier = Modifier.height(56.dp))
    }
}

@Composable
private fun ReadingListContent(
    readingList: List<Book>,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        items(readingList, key = { it.bookID }) { book ->
            ReadingItem(
                book = book,
                onClick = {
                    navController.navigate(BookShelfScreens.BookScreen.name + "/${book}")
                }
            )
        }
    }
}

@Composable
private fun ReadingItem(book: Book, onClick: () -> Unit) {
    val genre = book.categories.firstOrNull()?.split("/")?.minByOrNull { it.length }?.trim()
        .takeIf { !it.isNullOrBlank() } ?: stringResource(R.string.unavailable)

    book.imageLinks.thumbnail.let { rawUrl ->
        Reading(
            genre = genre,
            bookAuthor = book.authors.firstOrNull().orEmpty(),
            bookTitle = book.title,
            imageUrl = rawUrl.replace("http://", "https://"),
            rating = book.averageRating.toFloat(),
            onClick = onClick
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ReadingListPreview() {
    val context = LocalContext.current
    // ننشئ NavController فارغ بدون استخدام rememberNavController()
    val navController = NavController(context)

    // قائمة كتب وهمية
    val sampleBooks = listOf(
        Book(
            bookID = "1",
            authors = listOf("J.K. Rowling"),
            averageRating = 4.5,
            categories = listOf("Fantasy/Magic"),
            description = "A young wizard's journey begins.",
            imageLinks = ImageLinks(
                thumbnail = "https://via.placeholder.com/100",
                smallThumbnail = "https://via.placeholder.com/50"
            ),
            language = "en",
            pageCount = 223,
            industryIdentifiers = emptyList(),
            publishedDate = LocalDate.parse("1997-06-26"),  // تحويل للنمط الافتراضي ISO
            publisher = "Bloomsbury",
            ratingsCount = 12000,
            subtitle = "Philosopher's Stone",
            title = "Harry Potter and the Philosopher's Stone",
            searchInfo = ""
        ), Book(
            bookID = "2",
            authors = listOf("George Orwell"),
            averageRating = 4.2,
            categories = listOf("Dystopian/Political"),
            description = "A chilling prophecy about the future.",
            imageLinks = ImageLinks(
                thumbnail = "https://via.placeholder.com/100",
                smallThumbnail = "https://via.placeholder.com/50"
            ),
            language = "en",
            pageCount = 328,
            industryIdentifiers = emptyList(),
            publishedDate = LocalDate.parse("1949-06-08"),
            publisher = "Sicker & Wartburg",
            ratingsCount = 15000,
            subtitle = "",
            title = "1984",
            searchInfo = ""
        )
    )

    ReadingList(
        navController = navController,
        loading = false,
        readingList = sampleBooks,
        modifier = Modifier.fillMaxSize()
    )
}