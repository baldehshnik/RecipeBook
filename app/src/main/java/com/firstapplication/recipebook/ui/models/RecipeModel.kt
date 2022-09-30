package com.firstapplication.recipebook.ui.models

data class RecipeModel(
    val id: Long = 0L,
    var imageId: Int = -1,
    var title: String,
    var recipeInfo: String,

    var cookingTime: Double,
    var timeType: String,

    var ingredients: Map<String, String>,
    var isSaved: Boolean = false,
    var category: String = "Горячее блюдо"
)