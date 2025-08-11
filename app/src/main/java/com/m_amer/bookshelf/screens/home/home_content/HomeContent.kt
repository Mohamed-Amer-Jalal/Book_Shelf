package com.m_amer.bookshelf.screens.home.home_content

import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.m_amer.bookshelf.R
import com.m_amer.bookshelf.model.Book
import com.m_amer.bookshelf.screens.home.categories.Categories
import com.m_amer.bookshelf.screens.home.main_card.MainCard
import com.m_amer.bookshelf.screens.home.reading_list.ReadingList
import com.m_amer.bookshelf.screens.home.top_header.TopHeader
import com.m_amer.bookshelf.screens.searsh.SearchBookViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeContent(
    userName: String?,
    avatarUri: Bitmap?,
    navController: NavController,
    searchBookViewModel: SearchBookViewModel,
    imageDataStore: StoreProfileImage,
    sessionStore: StoreSession,
    readingList: List<Book>,
    loading: Boolean,
    onLogout: () -> Unit,
    onDeleteAccount: () -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentRead = readingList.firstOrNull() ?: Book()
    val totalReading = readingList.size

    // Photo picker launcher
    val context = LocalContext.current
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let { pickedUri ->
                // Persist permission and save
                context.contentResolver.takePersistableUriPermission(
                    pickedUri, Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                // Launch a coroutine to call suspend functions
                scope.launch {
                    try {
                        imageDataStore.saveImageUri(pickedUri) // Call your suspend function
                        sessionStore.markLaunched()          // Call this if it's also a suspend function
                        // If markLaunched is NOT suspend, it can be outside scope.launch
                        // or inside if it's related to the async operation.
                    } catch (e: Exception) {
                        // Handle exceptions, e.g., log them or show a message
                        Log.e(
                            "HomeContent",
                            "Error saving image URI or marking session: ${e.localizedMessage}"
                        )
                    }
                }
            }
        }

    ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
        DrawerContent(
            avatarUri = avatarUri,
            userName = userName,
            totalBooks = totalReading,
            onChangePhoto = { launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
            onLogout = onLogout,
            onDelete = onDeleteAccount
        )
    }, scrimColor = MaterialTheme.colorScheme.scrim, content = {
        Scaffold(topBar = {
            TopHeader(
                navController = navController,
                viewModel = searchBookViewModel,
                avatar = avatarUri,
                onProfileClick = { scope.launch { drawerState.open() } })
        }, bottomBar = {
            NavBar(navController)
        }) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MainCard(currentRead, navController, readingList)
                Categories(navController)
                ReadingList(navController, loading, readingList)
            }
        }
    })
}

@Composable
private fun DrawerContent(
    avatarUri: Bitmap?,
    userName: String?,
    totalBooks: Int,
    onChangePhoto: () -> Unit,
    onLogout: () -> Unit,
    onDelete: () -> Unit
) {
    ModalDrawerSheet(modifier = Modifier.width(280.dp)) {
        Spacer(modifier = Modifier.height(24.dp))
        ProfileSection(avatarUri = avatarUri, userName = userName, onChangePhoto = onChangePhoto)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.books_in_your_reading_list, totalBooks),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 12.dp),
            thickness = DividerDefaults.Thickness,
            color = DividerDefaults.color
        )
        DrawerItemsList(onLogout = onLogout, onDelete = onDelete)
        Spacer(modifier = Modifier.weight(1f))
        FooterInfo()
    }
}

@Composable
private fun ProfileSection(avatarUri: Bitmap?, userName: String?, onChangePhoto: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier
                .size(64.dp)
                .clickable(onClick = onChangePhoto),
            shape = CircleShape,
            tonalElevation = 2.dp
        ) {
            AsyncImage(
                model = avatarUri,
                contentDescription = stringResource(R.string.profile_picture),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "Hi, ${userName?.substringBefore(' ')}!",
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
private fun DrawerItemsList(onLogout: () -> Unit, onDelete: () -> Unit) {
    val items = listOf(
        DrawerItem(stringResource(R.string.log_out), Icons.AutoMirrored.Filled.ExitToApp, onLogout),
        DrawerItem(stringResource(R.string.delete_account), Icons.Default.Delete, onDelete)
    )
    items.forEach { item ->
        NavigationDrawerItem(
            icon = { Icon(item.icon, contentDescription = item.label) },
            label = { Text(item.label) },
            selected = false,
            onClick = item.action,
            colors = NavigationDrawerItemDefaults.colors(selectedContainerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}

@Composable
private fun FooterInfo() {
    Text(
        text = stringResource(R.string.developer_mohamed_amer),
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        textAlign = TextAlign.Center
    )
}

private data class DrawerItem(val label: String, val icon: ImageVector, val action: () -> Unit)