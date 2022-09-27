package com.firstapplication.recipebook.ui.interfacies

import android.view.View

interface OnIngredientDeleteItemClickListener {
    fun onIngredientDeleteItemClick(view: View, ingredient: Pair<String, String>)
}