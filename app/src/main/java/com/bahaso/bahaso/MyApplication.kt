package com.bahaso.bahaso

import android.app.Application
import com.bahaso.bahaso.core.di.CoreComponent
import com.bahaso.bahaso.core.di.DaggerCoreComponent
import com.bahaso.bahaso.di.AppComponent
import com.bahaso.bahaso.di.DaggerAppComponent
import timber.log.Timber

open class MyApplication : Application() {

    private val coreComponent: CoreComponent by lazy {
        DaggerCoreComponent.factory().create()
    }

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(coreComponent)
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}