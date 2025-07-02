package com.m_amer.bookshelf.screens.home.store_profileImage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * مسؤول عن تخزين وحفظ حالة أول مرة تشغيل للتطبيق باستخدام DataStore.
 *
 * @param dataStore الـ DataStore جاهز من خارج الكلاس (يُحقن عبر DI).
 * @param coroutineScope الـ scope الذي تُجرى فيه عمليات التخزين والمراقبة (افتراضيًا IO).
 */
class StoreSession(
    private val dataStore: DataStore<Preferences>,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) {

    companion object {
        private val IS_FIRST_TIME_LAUNCH = booleanPreferencesKey("is_first_time_launch")
    }

    // StateFlow تعرض القيمة الحالية وتبثّ التحديثات
    private val _isFirstTime = MutableStateFlow(true)
    val isFirstTime: StateFlow<Boolean> = _isFirstTime.asStateFlow()

    init {
        observeFirstLaunchFlag()
    }

    private fun observeFirstLaunchFlag() {
        // نراقب الـ DataStore ونعكس التغييرات في الـ StateFlow
        coroutineScope.launch {
            dataStore.data
                .map { prefs -> prefs[IS_FIRST_TIME_LAUNCH] ?: true }
                .distinctUntilChanged()
                .collect { value -> _isFirstTime.value = value }
        }
    }

    /**
     * لحفظ القيمة الجديدة عند تغيّر حالة أول تشغيل.
     */
    suspend fun setFirstTimeLaunch(isFirst: Boolean) {
        dataStore.edit { prefs -> prefs[IS_FIRST_TIME_LAUNCH] = isFirst }
    }
}