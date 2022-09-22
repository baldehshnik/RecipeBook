package com.firstapplication.recipebook.data.models

import androidx.room.*
import com.firstapplication.recipebook.data.local.IngredientsConverter

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

    @ColumnInfo(name = "cooking_time")
    val cookingTime: Double,

    @ColumnInfo(name = "ingredients_count")
    val ingredientsCount: Int = 0,

    @ColumnInfo(name = "ingredients")
    val ingredients: Map<String, String>,

    @ColumnInfo(name = "is_saved")
    val isSaved: Boolean = false
)