package com.m_amer.bookshelf.screens.home.categories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.m_amer.bookshelf.R
import com.m_amer.bookshelf.data.categories
import com.m_amer.bookshelf.navigation.BookShelfScreens
import com.m_amer.bookshelf.ui.theme.poppinsFamily

/**
 * Displays a horizontal list of categories.
 *
 * Each category is represented by an image and a name. When a category is clicked,
 * it navigates to the [BookShelfScreens.CategoryScreen] for that category.
 *
 * @param navController The [NavController] used for navigation.
 */
@Composable
fun Categories(navController: NavController) {
    Text(
        text = stringResource(R.string.categories),
        fontFamily = poppinsFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(top = 10.dp, start = 20.dp, end = 20.dp)
    )
    val keysList = categories.keys.toList()

    LazyRow(
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(items = keysList) { item ->
            categories[item]?.let { image ->
                Category(
                    category = item,
                    image = image,
                    onClick = {
                        navController.navigate("${BookShelfScreens.CategoryScreen.name}/$item")
                    }
                )
            }
        }
    }
}

/**
 * A composable function that displays a list of categories.
 *
 * @param categoriesMap A map of category names to image resource IDs.
 * @param onCategoryClick A lambda function that is called when a category is clicked.
 * @param modifier A [Modifier] for this composable.
 */
@Composable
private fun CategoriesContent(
    categoriesMap: Map<String, Int>,
    onCategoryClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Categories",
            fontFamily = poppinsFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(top = 10.dp, start = 20.dp, end = 20.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 0.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(categoriesMap.entries.toList(), key = { it.key }) { (category, imageRes) ->
                Category(
                    category = category,
                    image = imageRes,
                    onClick = { onCategoryClick(category) }
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CategoriesPreview() {
    val sampleCategories = mapOf(
        "Fantasy"    to R.drawable.emptyshelf,
        "Science"    to R.drawable.emptyshelf,
        "Philosophy" to R.drawable.emptyshelf
    )
    CategoriesContent(
        categoriesMap = sampleCategories,
        onCategoryClick = {  }
    )
}