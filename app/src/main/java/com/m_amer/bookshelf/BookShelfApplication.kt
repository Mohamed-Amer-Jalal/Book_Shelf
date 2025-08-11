package com.m_amer.bookshelf

import android.app.Application
import com.m_amer.bookshelf.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * Custom [Application] class for the BookShelf application.
 * This class is responsible for initializing Koin dependency injection.
 */
class BookShelfApplication : Application() {
    /**
     * Called when the application is starting, before any activity, service,
     * or receiver objects (excluding content providers) have been created.
     *
     * This is where you should initialize global application state, such as
     * setting up dependency injection with Koin.
     */
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BookShelfApplication)
            modules(appModule)
        }
    }
}