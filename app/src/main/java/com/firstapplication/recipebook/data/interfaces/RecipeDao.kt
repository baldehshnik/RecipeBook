package com.firstapplication.recipebook.data.interfaces

import androidx.lifecycle.LiveData
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
    fun readAllRecipesReversed(): LiveData<List<RecipeEntity>>

    @Query("SELECT * FROM recipes WHERE category = :category ORDER BY id DESC")
    fun readAllRecipesInCategoryReversed(category: String): LiveData<List<RecipeEntity>>

    @Query("SELECT * FROM recipes WHERE is_saved = 1")
    fun readAllSavedRecipesReversed(): LiveData<List<RecipeEntity>>

    @Query("SELECT * FROM recipes WHERE is_saved = 1 AND category = :category")
    fun readAllSavedRecipesInCategoryReversed(category: String): LiveData<List<RecipeEntity>>



    @Query("SELECT * FROM recipes WHERE lower(title) LIKE :string")
    fun readAllRecipesThatMatchFormat(string: String): LiveData<List<RecipeEntity>>

}