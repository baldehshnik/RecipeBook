package com.firstapplication.recipebook.extensions

import android.content.Context
import com.firstapplication.recipebook.App
import com.firstapplication.recipebook.di.AppComponent

val Context.appComponent: AppComponent get() = when(this) {
    is App -> appComponent
    else -> this.applicationContext.appComponent
}
