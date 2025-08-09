package com.m_amer.bookshelf.screens.searsh

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.m_amer.bookshelf.R
import com.m_amer.bookshelf.navigation.BookShelfScreens
import com.m_amer.bookshelf.ui.theme.poppinsFamily

@Composable
fun Search(navController: NavController, viewModel: SearchBookViewModel, onSearch: (String) -> Unit = {}) {
    val searchQuery = rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val isValid = searchQuery.value.trim().isNotEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // شريط علوي مع زر إغلاق
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = {
                navController.navigate(BookShelfScreens.HomeScreen.name) {
                    popUpTo(BookShelfScreens.HomeScreen.name) { inclusive = true }
                }
            }) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = stringResource(R.string.close_search),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.search),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = poppinsFamily
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // حقل البحث
        OutlinedTextField(
            value = searchQuery.value,
            onValueChange = { searchQuery.value = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.find_a_book)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    if (isValid) {
                        onSearch(searchQuery.value.trim())
                        keyboardController?.hide()
                        searchQuery.value = ""
                    }
                }
            ),
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (isValid) {
                            viewModel.searchBooks(searchQuery.value.trim())
                            keyboardController?.hide()
                            searchQuery.value = ""
                        }
                    }
                ) {
                    Icon(Icons.Default.Search, contentDescription = stringResource(R.string.search))
                }
            }
        )
    }
}