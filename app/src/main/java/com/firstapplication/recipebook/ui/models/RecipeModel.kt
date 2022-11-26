package com.firstapplication.recipebook.ui.models

import android.os.Parcelable
import com.firstapplication.recipebook.data.models.RecipeEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecipeModel(
    val id: Long = 0L,
    var imageId: Int = -1,
    var title: String,
    var recipeInfo: String,
    var time: String,
    var ingredients: Map<String, String>,
    var isSaved: Boolean = false,
    var category: String = "Горячие блюда"
) : Parcelable {

    fun migrateFromRecipeModelToRecipeEntity() = RecipeEntity(
        id = id,
        imageId = imageId,
        title = title,
        recipeInfo = recipeInfo,
        time = time,
        ingredients = ingredients,
        isSaved = isSaved,
        category = category
    )

}