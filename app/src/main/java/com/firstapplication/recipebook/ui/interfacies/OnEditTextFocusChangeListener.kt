package com.firstapplication.recipebook.ui.interfacies

import android.widget.EditText

interface OnEditTextFocusChangeListener {
    fun setOnFocusChangeListener(hasFocus: Boolean, editText: EditText)
}