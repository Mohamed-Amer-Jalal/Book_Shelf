package com.m_amer.bookshelf.screens.home.categories

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m_amer.bookshelf.R
import com.m_amer.bookshelf.ui.theme.poppinsFamily

@Composable
fun Category(
    category: String,
    image: Int,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.wrapContentSize()
    ) {
        Surface(
            modifier = Modifier
                .size(50.dp)
                .clickable(onClick = onClick),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.secondary,
            tonalElevation = 2.dp,
            shadowElevation = 2.dp
        ) {
            Image(
                painter = painterResource(id = image),
                contentDescription = "Category",
                contentScale = ContentScale.Crop,
                modifier = Modifier.clip(CircleShape)
            )
        }
        Text(
            text = category,
            fontFamily = poppinsFamily,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
            modifier = Modifier.padding(top = 5.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryPreview() {
    Category(
        category = "Fantasy",
        image = R.drawable.finance,
        onClick = { /* Handle click event */ }
    )
}