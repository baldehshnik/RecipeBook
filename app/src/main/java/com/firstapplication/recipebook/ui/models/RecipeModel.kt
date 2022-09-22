package com.firstapplication.recipebook.ui.models

data class RecipeModel(
    val id: Long = 0L, val imageId: Int,
    val title: String, val recipeInfo: String,
    val cookingTime: Double, val ingredientsCount: Int = 0,
    val ingredients: Map<String, String>, val isSaved: Boolean = false
) {



}