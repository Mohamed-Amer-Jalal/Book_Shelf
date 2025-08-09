package com.m_amer.bookshelf

import android.app.Application
import com.m_amer.bookshelf.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BookShelfApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BookShelfApplication)
            modules(appModule)
        }
    }
}