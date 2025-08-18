package com.m_amer.bookshelf.screens.home.top_header

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.m_amer.bookshelf.R
import com.m_amer.bookshelf.navigation.BookShelfScreens
import com.m_amer.bookshelf.screens.searsh.SearchBookViewModel

/**
 * Composable function that displays the top header of the home screen.
 * It includes the user's avatar, which is clickable to navigate to the profile screen,
 * and a search icon, which navigates to the search screen.
 *
 * @param navController The NavController used for navigation.
 * @param viewModel The SearchBookViewModel used to manage search results state.
 * @param avatar The user's avatar bitmap.
 * @param onProfileClick A lambda function to be executed when the profile avatar is clicked.
 */
@Composable
fun TopHeader(
    navController: NavController,
    viewModel: SearchBookViewModel,
    avatar: Bitmap?,
    onProfileClick: () -> Unit
) {
    var resultsState = viewModel.resultsState.value
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            bitmap = avatar!!.asImageBitmap(),
            contentDescription = stringResource(R.string.profile_picture),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .clickable(onClick = onProfileClick)
        )

        IconButton(
            onClick = {
                resultsState = resultsState.copy(data = listOf(), loading = false, e = null)

                navController.navigate(BookShelfScreens.SearchScreen.name)
            },
            modifier = Modifier
                .size(48.dp)
                .border(
                    width = 0.9.dp,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search",
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}