package com.m_amer.bookshelf.screens.home.home_content

import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.Bitmap
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.m_amer.bookshelf.R
import com.m_amer.bookshelf.model.Book
import com.m_amer.bookshelf.navigation.BookShelfScreens
import com.m_amer.bookshelf.screens.home.categories.Categories
import com.m_amer.bookshelf.screens.home.main_card.MainCard
import com.m_amer.bookshelf.screens.home.reading_list.ReadingList
import com.m_amer.bookshelf.screens.home.store_profileImage.StoreProfileImage
import com.m_amer.bookshelf.screens.home.store_profileImage.StoreSession
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/*
  Refactored HomeContent composable:
  - Extracted DrawerContent into its own composable
  - Hoisted state and minimized recompositions
  - Simplified image picking logic with a reusable ProfileImagePicker
  - Applied consistent styling and removed redundant code
*/

@Composable
fun HomeContent(
    user: FirebaseUser,
    name: String?,
    avatar: Bitmap,
    navController: NavController,
    searchBookViewModel: SearchBookViewModel,
    imageDataStore: StoreProfileImage,
    session: StoreSession,
    reading: List<Book>,
    loading: Boolean
) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    // Manage profile image state
    var selectedImageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val profileImageBitmap by remember(selectedImageUri, avatar) {
        mutableStateOf(
            selectedImageUri?.let { uri ->
                ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri))
            } ?: avatar
        )
    }

    // Launch image picker
    val pickImageLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            context.contentResolver.takePersistableUriPermission(
                it, Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            selectedImageUri = it

            scope.launch {
                imageDataStore.saveImagePath(it)
                session.setFirstTimeLaunch(false)
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                name = name,
                avatarBitmap = profileImageBitmap,
                isFirstSession = session.isFirstTime,
                onPickImage = { pickImageLauncher.launch(arrayOf("image/*")) },
                readingCount = reading.size,
                onLogout = {
                    Firebase.auth.signOut()
                    navController.navigate(BookShelfScreens.HomeScreen.name)
                },
                onDeleteAccount = {
                    user.delete()
                    navController.navigate(BookShelfScreens.HomeScreen.name)
                }
            )
        },
        scrimColor = Color.Transparent
    ) {
        Scaffold(
            topBar = {
                HomeTopBar(
                    avatar = avatar,
                    onMenuClick = { scope.launch { drawerState.open() } },
                    navController = navController,
                    viewModel = searchBookViewModel
                )
            },
            bottomBar = { NavBar(navController) }
        ) { padding ->
            Column(
                Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                reading.firstOrNull()?.let { currentBook ->
                    MainCard(currentBook, navController)
                }
                Categories(navController)
                ReadingList(navController, loading, reading)
            }
        }
    }
}

@Composable
private fun DrawerContent(
    name: String?,
    avatarBitmap: Bitmap,
    isFirstSession: StateFlow<Boolean>,
    onPickImage: () -> Unit,
    readingCount: Int,
    onLogout: () -> Unit,
    onDeleteAccount: () -> Unit
) {
    Column(modifier = Modifier
        .fillMaxHeight()
        .width(280.dp)
        .padding(16.dp)) {

        ProfileImagePicker(
            bitmap = avatarBitmap,
            onClick = onPickImage
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Hi, ${name?.substringBefore(" ") ?: "Reader"}!",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(24.dp))
        Text(
            text = "$readingCount books in your list",
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(Modifier.height(16.dp))
        Divider()
        Spacer(Modifier.height(16.dp))

        DrawerActionItem(
            icon = R.drawable.logout,
            label = "Log Out",
            onClick = onLogout
        )
        DrawerActionItem(
            icon = R.drawable.delete,
            label = "Delete Account",
            onClick = onDeleteAccount
        )

        Spacer(Modifier.weight(1f))
        Text(
            text = "Developer: Lynne Munini",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.align(Alignment.End)
        )
    }
}

@Composable
private fun ProfileImagePicker(
    bitmap: Bitmap,
    onClick: () -> Unit
) {
    Box {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Profile picture",
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .clickable { onClick() },
            contentScale = ContentScale.Crop
        )
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = "Edit",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset((-4).dp, (-4).dp)
                .size(20.dp)
                .background(MaterialTheme.colorScheme.primary, CircleShape)
                .clickable { onClick() }
                .padding(4.dp)
        )
    }
}

@Composable
private fun DrawerActionItem(
    @DrawableRes icon: Int,
    label: String,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        icon = { Icon(painterResource(id = icon), contentDescription = label) },
        label = { Text(label) },
        selected = false,
        onClick = onClick,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}
