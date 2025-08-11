package com.m_amer.bookshelf.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.m_amer.bookshelf.R
import com.m_amer.bookshelf.navigation.BookShelfScreens
import kotlinx.coroutines.delay

/**
 * Composable function that displays a splash screen.
 *
 * The splash screen shows an image that fades in and then navigates to a specified route.
 *
 * @param navController The NavController used for navigation.
 * @param modifier Modifier to be applied to the Surface of the splash screen.
 * @param routeOnFinish The route to navigate to after the splash screen animation finishes.
 *                      Defaults to the HomeScreen.
 */
@Composable
fun SplashScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    routeOnFinish: String = BookShelfScreens.HomeScreen.name
) {
    var startAnimation by remember { mutableStateOf(false) }

    val alpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000)
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(2000L)
        navController.navigate(routeOnFinish) {
            popUpTo(navController.graph.startDestinationId) { inclusive = true }
            launchSingleTop = true
        }
    }

    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(R.drawable.book),
                contentDescription = stringResource(R.string.splash_book),
                modifier = Modifier
                    .size(128.dp)
                    .alpha(alpha),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )
        }
    }
}