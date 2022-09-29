package com.firstapplication.recipebook.ui.interfacies

import android.view.View
import com.firstapplication.recipebook.sealed.RecipeListItemClick
import com.firstapplication.recipebook.ui.models.RecipeModel

interface OnRecipeItemClickListener {
    fun onItemClick(view: View, recipeModel: RecipeModel, recipeKey: RecipeListItemClick)
    fun onItemLongClick(view: View, recipeModel: RecipeModel): Boolean
}