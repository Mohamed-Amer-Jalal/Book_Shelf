package com.m_amer.bookshelf.screens.home.store_profileImage

import android.content.Context
import android.net.Uri
import androidx.datastore.core.DataStore
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.dsl.module


/**
 * A class to store and retrieve a user's profile image path using DataStore.
 */
class StoreProfileImage(private val dataStore: DataStore<Preferences>) {
    companion object {
        // Preference key for the image path
        val USER_IMAGE_KEY = stringPreferencesKey("user_image")
    }

    /**
     * Flow to observe the saved image path from DataStore.
     * Emits an empty string if no image is saved.
     */
    val imagePathFlow: Flow<String> =
        dataStore.data.map { preferences -> preferences[USER_IMAGE_KEY] ?: "" }

    /**
     * Saves the URI as a string in DataStore.
     * @param uri The URI to save.
     */
    suspend fun saveImagePath(uri: Uri) {
        dataStore.edit { preferences -> preferences[USER_IMAGE_KEY] = uri.toString() }
    }
}

val dataStoreModule = module {
    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.create(
            produceFile = { get<Context>().dataStoreFile("profileImage") }
        )
    }

    single { StoreProfileImage(get()) }
}
