package com.m_amer.bookshelf.screens.home.home_content

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


/**
 * Manages the first-launch state of the application using DataStore Preferences.
 * @param dataStore injected DataStore instance for Preferences.
 */
class StoreSession(private val dataStore: DataStore<Preferences>) {
    companion object Keys {
        /** Preferences key indicating if it's the first app launch. */
        val IS_FIRST_TIME_LAUNCH = booleanPreferencesKey("is_first_time_launch")
    }

    /**
     * Exposes a Flow that emits true if it's the first launch, false otherwise.
     * Collect this in your UI layer to reactively update.
     */
    val isFirstTimeFlow: Flow<Boolean> =
        dataStore.data.map { preferences -> preferences[IS_FIRST_TIME_LAUNCH] ?: true }

    /**
     * Marks that the app has been launched at least once.
     */
    suspend fun markLaunched() =
        dataStore.edit { preferences -> preferences[IS_FIRST_TIME_LAUNCH] = false }

}
