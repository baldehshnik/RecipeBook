package com.firstapplication.recipebook.utils

import android.app.Application
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import com.firstapplication.recipebook.ui.interfacies.Navigator

class MainNavigator(
    application: Application
) : AndroidViewModel(application), Navigator {

    override fun toast(@StringRes messId: Int) {
        Toast.makeText(getApplication(), messId, Toast.LENGTH_SHORT).show()
    }
}