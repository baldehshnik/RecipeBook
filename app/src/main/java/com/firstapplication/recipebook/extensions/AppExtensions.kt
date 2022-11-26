package com.firstapplication.recipebook.extensions

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import com.firstapplication.recipebook.App
import com.firstapplication.recipebook.di.AppComponent

val Context.appComponent: AppComponent
    get() = when (this) {
        is App -> appComponent
        else -> this.applicationContext.appComponent
    }

fun InputMethodManager.closeKeyboard(activity: Activity?) {
    hideSoftInputFromWindow(activity?.window?.decorView?.windowToken, 0)
}