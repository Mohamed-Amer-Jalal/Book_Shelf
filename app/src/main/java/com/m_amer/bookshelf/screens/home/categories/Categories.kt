package com.m_amer.bookshelf.screens.home.categories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.m_amer.bookshelf.R
import com.m_amer.bookshelf.data.categories
import com.m_amer.bookshelf.navigation.BookShelfScreens
import com.m_amer.bookshelf.ui.theme.poppinsFamily

@Composable
fun Categories(navController: NavController) {
    Text(
        text = stringResource(R.string.categories),
        fontFamily = poppinsFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier
            .padding(top = 10.dp, start = 20.dp, end = 20.dp)
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

/*
@Composable
fun CategoriesContent(
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

*/
/**معاينة الواجهة بدون NavController *//*

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
        onCategoryClick = { */
/* no-op for preview *//*
 }
    )
}*/