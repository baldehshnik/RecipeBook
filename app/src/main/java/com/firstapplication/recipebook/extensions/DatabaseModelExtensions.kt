package com.firstapplication.recipebook.extensions

import com.firstapplication.recipebook.data.models.RecipeEntity
import com.firstapplication.recipebook.ui.models.RecipeModel

fun RecipeEntity.migrateFromRecipeEntityToRecipeModel() = RecipeModel(
    id = id,
    imageId = imageId,
    title = title,
    recipeInfo = recipeInfo,
    time = time,
    ingredients = ingredients,
    isSaved = isSaved,
    category = category
)

fun RecipeModel.migrateFromRecipeModelToRecipeEntity() = RecipeEntity(
    id = id,
    imageId = imageId,
    title = title,
    recipeInfo = recipeInfo,
    time = time,
    ingredients = ingredients,
    isSaved = isSaved,
    category = category
)