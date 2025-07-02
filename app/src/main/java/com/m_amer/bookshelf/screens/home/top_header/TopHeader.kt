package com.m_amer.bookshelf.screens.home.top_header

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
import coil3.Bitmap
import com.m_amer.bookshelf.R
import com.m_amer.bookshelf.navigation.BookShelfScreens

@Composable
fun TopHeader(
    navController: NavController,
    viewModel: SearchBookViewModel,
    avatar: Bitmap?,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // زر الصورة الشخصيّة
        Image(
            bitmap = avatar.asImageBitmap(),
            contentDescription = stringResource(R.string.profile_picture),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .clickable(onClick = onProfileClick)
        )

        // زر البحث
        IconButton(
            onClick = {
                viewModel.loading.value = false
                viewModel.listOfBooks.value = emptyList()
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