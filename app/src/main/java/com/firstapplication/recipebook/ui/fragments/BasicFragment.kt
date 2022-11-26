package com.firstapplication.recipebook.ui.fragments

import android.widget.Toast
import androidx.annotation.ArrayRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

open class BasicFragment: Fragment() {

    fun toast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    fun getStringFromRes(@StringRes stringRes: Int): String  {
        return resources.getString(stringRes)
    }

    fun getStringArrayFromRes(@ArrayRes arrayRes: Int): Array<String> {
        return resources.getStringArray(arrayRes)
    }

}