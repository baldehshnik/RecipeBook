package com.firstapplication.recipebook.extensions

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.firstapplication.recipebook.App
import com.firstapplication.recipebook.data.models.RecipeEntity
import com.firstapplication.recipebook.di.AppComponent
import com.firstapplication.recipebook.ui.models.RecipeModel
import kotlinx.coroutines.async
import java.lang.StringBuilder

val Context.appComponent: AppComponent
    get() = when (this) {
        is App -> appComponent
        else -> this.applicationContext.appComponent
    }

fun InputMethodManager.closeKeyboard(activity: Activity?) {
    hideSoftInputFromWindow(
        activity?.window?.decorView?.windowToken, 0
    )
}

fun RecipeEntity.migrateFromRecipeEntityToRecipeModel() = RecipeModel(
    id = id,
    imageId = imageId,
    title = title,
    recipeInfo = recipeInfo,
    cookingTime = cookingTime,
    timeType = timeType,
    ingredients = ingredients,
    isSaved = isSaved,
    category = category
)

fun RecipeModel.migrateFromRecipeModelToRecipeEntity() = RecipeEntity(
    id = id,
    imageId = imageId,
    title = title,
    recipeInfo = recipeInfo,
    cookingTime = cookingTime,
    timeType = timeType,
    ingredients = ingredients,
    isSaved = isSaved,
    category = category
)

suspend fun AndroidViewModel.getMigratedToRecipeModelList(
    entitiesList: List<RecipeEntity>
): List<RecipeModel> {
    val list = mutableListOf<RecipeModel>()

    val coroutineResult = this.viewModelScope.async {
        entitiesList.forEach { item ->
            list.add(item.migrateFromRecipeEntityToRecipeModel())
        }
    }

    coroutineResult.await()

    return list
}