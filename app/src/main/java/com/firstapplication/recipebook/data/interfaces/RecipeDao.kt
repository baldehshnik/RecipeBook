package com.firstapplication.recipebook.data.interfaces

import androidx.room.*
import com.firstapplication.recipebook.data.models.RecipeEntity

@Dao
interface RecipeDao {

    @Update
    suspend fun update(recipeEntity: RecipeEntity)

    @Insert
    suspend fun insert(recipeEntity: RecipeEntity)

    @Delete
    suspend fun delete(recipeEntity: RecipeEntity)

    @Query("SELECT * FROM recipes ORDER BY id DESC")
    fun readAllRecipesReversed(): List<RecipeEntity>

    @Query("SELECT * FROM recipes WHERE category = :category ORDER BY id DESC")
    fun readAllRecipesInCategoryReversed(category: String): List<RecipeEntity>

    @Query("SELECT * FROM recipes WHERE is_saved = 1")
    fun readAllSavedRecipes(): List<RecipeEntity>

    @Query("SELECT * FROM recipes WHERE is_saved = 1 AND category = :category")
    fun readAllSavedRecipesInCategory(category: String): List<RecipeEntity>

    @Query("SELECT * FROM recipes WHERE lower(title) LIKE :string")
    fun readAllRecipesThatMatchFormat(string: String): List<RecipeEntity>

    @Query("SELECT * FROM recipes WHERE lower(title) LIKE :string AND is_saved = 1")
    fun readAllSavedRecipesThatMatchFormat(string: String): List<RecipeEntity>

}