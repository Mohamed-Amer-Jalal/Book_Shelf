package com.m_amer.bookshelf.screens.searsh

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.m_amer.bookshelf.R
import com.m_amer.bookshelf.ui.theme.Yellow
import com.m_amer.bookshelf.ui.theme.poppinsFamily

/**
 * Composable function for the search screen.
 *
 * This screen allows users to search for books and view their search history.
 *
 * @param navController The NavController used for navigation.
 * @param viewModel The SearchBookViewModel used to manage search state and data.
 */
@Composable
fun SearchScreen(navController: NavController, viewModel: SearchBookViewModel) {
    val userId = Firebase.auth.currentUser?.uid
    var previousSearches by remember { mutableStateOf(listOf<String>()) }
    var displayPreviousHistory by remember { mutableStateOf(false) }

    // جلب بيانات المستخدم مرة واحدة فقط
    LaunchedEffect(userId) {
        if (userId != null) {
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener { doc ->
                    val data = (doc.get("searchHistory") as? List<*>)
                        ?.mapNotNull { it as? String }
                        ?: emptyList()
                    previousSearches = data
                    displayPreviousHistory = data.isNotEmpty()
                }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Search(navController = navController, viewModel = viewModel) { query ->
            viewModel.apply {
                resultsState.value.loading = true
                searchBooks(query)
            }

            if (userId != null) {
                FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(userId)
                    .get()
                    .addOnSuccessListener { doc ->
                        val history = (doc.get("searchHistory") as? List<*>) // safe cast to List<*>
                            ?.mapNotNull { it as? String } // filter only strings
                            ?.toMutableList() // make it mutable
                            ?: mutableListOf()

                        history.add(query)

                        FirebaseFirestore.getInstance()
                            .collection("users")
                            .document(userId)
                            .update("searchHistory", history)
                    }
            }
        }

        if (displayPreviousHistory) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    stringResource(R.string.recent_searches),
                    fontSize = 13.sp,
                    fontFamily = poppinsFamily,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val items = previousSearches
                        .takeLast(3) // آخر 3 بس
                        .distinct()
                        .asReversed()

                    items.forEach {
                        HistoryCard(text = it) {
                            viewModel.apply {
                                resultsState.value.loading = true
                                searchBooks(it)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Results(viewModel = viewModel, navController = navController)
    }
}

/**
 * A composable function that displays a card with a search history item.
 *
 * @param text The text to display in the card, representing a previous search query.
 * @param onClick A lambda function to be executed when the card is clicked. This typically triggers a new search with the history item's text.
 */
@Composable
private fun HistoryCard(text: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .clickable(onClick = onClick)
            .heightIn(min = 30.dp)
            .padding(vertical = 2.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Yellow),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontFamily = poppinsFamily,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                textAlign = TextAlign.Center
            )
        }
    }
}