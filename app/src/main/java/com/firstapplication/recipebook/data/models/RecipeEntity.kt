package com.firstapplication.recipebook.data.models

import androidx.room.*
import com.firstapplication.recipebook.data.local.IngredientsConverter
import com.firstapplication.recipebook.ui.models.RecipeModel

@Entity(tableName = "recipes")
@TypeConverters(IngredientsConverter::class)
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(name = "image_id")
    val imageId: Int,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "recipe_info")
    val recipeInfo: String,

    @ColumnInfo(name = "time")
    val time: String,

    @ColumnInfo(name = "ingredients")
    val ingredients: Map<String, String>,

    @ColumnInfo(name = "is_saved")
    val isSaved: Boolean = false,

    @ColumnInfo(name = "category")
    val category: String = "Горячие блюда"
) {

    fun toRecipeModel(): RecipeModel {
        return RecipeModel(id, imageId, title, recipeInfo, time, ingredients, isSaved, category)
    }
}