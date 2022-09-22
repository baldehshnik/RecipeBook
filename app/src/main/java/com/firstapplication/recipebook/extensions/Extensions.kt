package com.firstapplication.recipebook.extensions

import android.content.Context
import com.firstapplication.recipebook.App
import com.firstapplication.recipebook.data.models.RecipeEntity
import com.firstapplication.recipebook.di.AppComponent
import com.firstapplication.recipebook.ui.models.RecipeModel

val Context.appComponent: AppComponent
    get() = when (this) {
        is App -> appComponent
        else -> this.applicationContext.appComponent
    }

fun RecipeEntity.migrateFromRecipeEntityToRecipeModel() = RecipeModel(
    id = id,
    imageId = imageId,
    title = title,
    recipeInfo = recipeInfo,
    cookingTime = cookingTime,
    ingredientsCount = ingredientsCount,
    ingredients = ingredients,
    isSaved = isSaved
)

fun RecipeModel.migrateFromRecipeModelToRecipeEntity() = RecipeEntity(
    id = id,
    imageId = imageId,
    title = title,
    recipeInfo = recipeInfo,
    cookingTime = cookingTime,
    ingredientsCount = ingredientsCount,
    ingredients = ingredients,
    isSaved = isSaved
)