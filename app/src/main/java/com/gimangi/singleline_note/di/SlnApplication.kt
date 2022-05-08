package com.gimangi.singleline_note.di

import android.app.Application
import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class SlnApplication : Application() {

    companion object {
        var instance: SlnApplication? = null
        fun context(): Context = instance!!.applicationContext
    }

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@SlnApplication)
            modules(ViewModelModule)
        }
    }
}