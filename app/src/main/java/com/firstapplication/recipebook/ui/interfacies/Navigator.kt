package com.firstapplication.recipebook.ui.interfacies

import androidx.annotation.StringRes

interface Navigator {
    fun toast(@StringRes messId: Int)
}