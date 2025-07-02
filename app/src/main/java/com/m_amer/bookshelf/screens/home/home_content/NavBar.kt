package com.m_amer.bookshelf.screens.home.home_content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.m_amer.bookshelf.navigation.BookShelfScreens
import com.m_amer.bookshelf.ui.theme.poppinsFamily

/**
 * A bottom navigation bar with dynamic items defined in a sealed class.
 */
@Composable
fun NavBar(navController: NavController, items: List<BottomNavItem> = defaultNavItems) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
        tonalElevation = 0.dp,
        modifier = Modifier.height(IntrinsicSize.Min)
    ) {
        items.forEach { item ->
            val selected = backStackEntry?.destination?.route == item.route
            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (!selected) navController.navigate(item.route) {
                        // Pop up to the start destination to avoid building up a large back stack
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        modifier = Modifier
                            .size(28.dp)
                            .background(color = MaterialTheme.colorScheme.background)
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontFamily = poppinsFamily,
                        fontSize = 12.sp
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    indicatorColor = MaterialTheme.colorScheme.surfaceColorAtElevation(0.dp)
                )
            )
        }
    }
}

/**
 * Data model for navigation items.
 */
sealed class BottomNavItem(val label: String, val route: String, val icon: ImageVector) {
    object Home : BottomNavItem("Home", BookShelfScreens.HomeScreen.name, Icons.Default.Home)
    object Shelves : BottomNavItem("Shelves", BookShelfScreens.ShelfScreen.name, Icons.Default.Book)
    object Favourites :
        BottomNavItem("Favourites", BookShelfScreens.FavouriteScreen.name, Icons.Default.Favorite)

    object Reviews :
        BottomNavItem("Reviews", BookShelfScreens.ReviewScreen.name, Icons.Default.Star)
}

/**
 * Default list of navigation items.
 */
val defaultNavItems = listOf(
    BottomNavItem.Home,
    BottomNavItem.Shelves,
    BottomNavItem.Favourites,
    BottomNavItem.Reviews
)