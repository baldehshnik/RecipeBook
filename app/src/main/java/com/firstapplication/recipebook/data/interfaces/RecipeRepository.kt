package com.firstapplication.recipebook.data.interfaces

import androidx.lifecycle.LiveData
import com.firstapplication.recipebook.data.models.RecipeEntity

interface RecipeRepository {
    suspend fun updateRecipe(recipeEntity: RecipeEntity)
    suspend fun insertRecipe(recipeEntity: RecipeEntity)
    suspend fun deleteRecipe(recipeEntity: RecipeEntity)

    fun readAllRecipesInCategory(category: String) : LiveData<List<RecipeEntity>>

    fun readAllSavedRecipes(category: String) : LiveData<List<RecipeEntity>>

    fun readRecipesMatchFormat(format: String) : LiveData<List<RecipeEntity>>

    fun readSavedRecipesMatchFormat(format: String) : LiveData<List<RecipeEntity>>
}