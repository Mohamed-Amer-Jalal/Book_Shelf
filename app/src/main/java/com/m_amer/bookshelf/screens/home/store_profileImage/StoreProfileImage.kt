package com.m_amer.bookshelf.screens.home.store_profileImage

import android.content.Context
import android.net.Uri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.io.IOException

/**
 * Repository to store and retrieve a user's profile image URI using DataStore.
 */
class StoreProfileImage(
    private val context: Context,
    ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    companion object {
        private val USER_IMAGE_KEY = stringPreferencesKey("user_image")

        // Extension property to initialize DataStore
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("profileImage")
    }

    /**
     * Flow emitting the saved image URI, or null if not set.
     */
    val imageUriFlow: Flow<Uri?> = context.dataStore.data
        .catch { exception ->
            // Emit empty preferences on error to avoid crashing
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }.map { preferences -> preferences[USER_IMAGE_KEY]?.let(Uri::parse) }.flowOn(ioDispatcher)

    /**
     * Saves the given image URI to DataStore.
     * @param uri The URI to save.
     */
    suspend fun saveImageUri(uri: Uri) =
        context.dataStore.edit { preferences -> preferences[USER_IMAGE_KEY] = uri.toString() }

    /**
     * Clears the saved image URI from DataStore.
     */
    suspend fun clearImageUri() =
        context.dataStore.edit { preferences -> preferences.remove(USER_IMAGE_KEY) }
}
