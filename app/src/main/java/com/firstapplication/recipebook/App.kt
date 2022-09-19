package com.firstapplication.recipebook

import android.app.Application
import com.firstapplication.recipebook.di.AppComponent
import com.firstapplication.recipebook.di.DaggerAppComponent

class App : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .setContext(applicationContext)
            .build()
    }
}