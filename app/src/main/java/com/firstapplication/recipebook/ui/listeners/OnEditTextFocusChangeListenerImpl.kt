package com.firstapplication.recipebook.ui.listeners

import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.firstapplication.recipebook.ui.interfacies.OnEditTextFocusChangeListener
import javax.inject.Inject

class OnEditTextFocusChangeListenerImpl @Inject constructor(
    private val inputMethodManager: InputMethodManager
) : OnEditTextFocusChangeListener {

    override fun setOnFocusChangeListener(hasFocus: Boolean, editText: EditText) {
        if (hasFocus) inputMethodManager.showSoftInput(
            editText,
            InputMethodManager.SHOW_FORCED
        )
        else inputMethodManager.hideSoftInputFromWindow(
            editText.windowToken,
            0
        )
    }
}