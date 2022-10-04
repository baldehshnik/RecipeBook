package com.firstapplication.recipebook.data.interfaces

import androidx.lifecycle.LiveData
import com.firstapplication.recipebook.data.models.RecipeEntity

interface RecipeRepository {
    suspend fun updateRecipe(recipeEntity: RecipeEntity)
    suspend fun insertRecipe(recipeEntity: RecipeEntity)
    suspend fun deleteRecipe(recipeEntity: RecipeEntity)

    fun allRecipesInCategory(category: String) : LiveData<List<RecipeEntity>>

    fun allSavedRecipes(category: String) : LiveData<List<RecipeEntity>>

    fun readRecipesMatchFormat(string: String) : LiveData<List<RecipeEntity>>

    fun readSavedRecipesMatchFormat(string: String) : LiveData<List<RecipeEntity>>
}