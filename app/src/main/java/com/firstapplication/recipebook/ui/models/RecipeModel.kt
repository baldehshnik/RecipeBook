package com.firstapplication.recipebook.ui.models

data class RecipeModel(
    val id: Long = 0L,
    val imageId: Int = -1,
    val title: String,
    val recipeInfo: String,
    val cookingTime: Double,
    val timeType: String,
    val ingredients: Map<String, String>,
    val isSaved: Boolean = false,
    val category: String = "Горячее блюдо"
)