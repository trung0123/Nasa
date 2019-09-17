package com.example.nasa

import android.app.Application
import com.example.nasa.di.app.AppComponent
import com.example.nasa.di.app.DaggerAppComponent
import com.example.nasa.di.misc.ComponentProvider
import timber.log.Timber

class NasaAPod : Application(), ComponentProvider {

    override val component: AppComponent by lazy {
        DaggerAppComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}
