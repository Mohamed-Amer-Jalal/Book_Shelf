package com.m_amer.bookshelf.screens.home.reading_list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.StarHalf
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.m_amer.bookshelf.R

/**
 * Composable function that displays a reading list item.
 *
 * This function creates a surface with rounded corners and tonal elevation,
 * displaying book information including genre, title, author, cover image, and rating.
 * It also includes a "Read More" button. The entire item is clickable.
 *
 * @param genre The genre of the book.
 * @param bookAuthor The author of the book.
 * @param bookTitle The title of the book.
 * @param imageUrl The URL of the book's cover image.
 * @param rating The rating of the book (e.g., 4.5f).
 * @param onClick Lambda function to be executed when the item is clicked.
 * @param modifier Optional [Modifier] to be applied to the composable.
 */
@Composable
fun Reading(
    genre: String,
    bookAuthor: String,
    bookTitle: String,
    imageUrl: String,
    rating: Float,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clickable(onClick = onClick)
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        shape = RoundedCornerShape(8.dp),
        tonalElevation = 2.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BookImage(url = imageUrl, onClick = onClick)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = genre,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = bookTitle,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "by $bookAuthor",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                StarRating(rating = rating)
            }
            ReadMoreButton(onClick = onClick)
        }
    }
}

/**
 * Displays an image for a book.
 *
 * This composable function takes a URL string for the image and an onClick lambda.
 * It uses [AsyncImage] to load the image asynchronously and shows a [CircularProgressIndicator]
 * while the image is loading. The image is clipped to a rounded rectangle and has a
 * background color.
 *
 * @param url The URL string of the book image.
 * @param onClick A lambda function to be executed when the image is clicked.
 */
@Composable
private fun BookImage(url: String, onClick: () -> Unit) {
    var isLoading by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .size(72.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(url)
                .crossfade(true)
                .build(),
            contentDescription = stringResource(R.string.book_cover),
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize(),
            onSuccess = { isLoading = false },
            onError = { isLoading = false }
        )
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                strokeWidth = 2.dp,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

/**
 * Composable function that displays a star rating.
 *
 * @param rating The rating value to display (e.g., 4.5f).
 * @param maxStars The maximum number of stars to display (default is 5).
 * @param starSize The size of each star icon (default is 14.dp).
 * @param filledColor The color of the filled stars (default is Color(0xFFFFC107)).
 */
@Composable
private fun StarRating(
    rating: Float,
    maxStars: Int = 5,
    starSize: Dp = 14.dp,
    filledColor: Color = Color(0xFFFFC107)
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        val fullStars = rating.toInt().coerceIn(0, maxStars)
        val hasHalf = (rating - fullStars) >= 0.5f
        val halfIndex = if (hasHalf) fullStars else -1

        (0 until maxStars).forEach { index ->
            val (icon, tint) = when {
                index < fullStars -> Icons.Rounded.Star to filledColor

                index == halfIndex -> Icons.AutoMirrored.Rounded.StarHalf to filledColor

                else -> Icons.Rounded.Star to Color.Gray.copy(alpha = 0.5f)
            }
            Icon(
                imageVector = icon,
                contentDescription = stringResource(R.string.star),
                modifier = Modifier.size(starSize),
                tint = tint
            )
        }
    }
}

/**
 * Composable function that displays a "Read More" button with a forward arrow icon.
 *
 * @param onClick Lambda function to be executed when the button is clicked.
 */
@Composable
private fun ReadMoreButton(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(start = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.read_more),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.secondary
        )
        Icon(
            imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
            contentDescription = null,
            modifier = Modifier
                .size(16.dp)
                .padding(start = 4.dp),
            tint = MaterialTheme.colorScheme.secondary
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ReadingPreview() {
    Reading(
        genre = "Fantasy",
        bookAuthor = "J.K. Rowling",
        bookTitle = "Harry Potter and the Philosopher's Stone",
        imageUrl = "https://via.placeholder.com/150",
        rating = 4.5f,
        onClick = {},
        modifier = Modifier.padding(16.dp)
    )
}