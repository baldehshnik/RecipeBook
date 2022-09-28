package com.firstapplication.recipebook.data.interfaces

import androidx.lifecycle.LiveData
import com.firstapplication.recipebook.data.models.RecipeEntity

interface RecipeRepository {
    suspend fun insertRecipe(recipeEntity: RecipeEntity)
    suspend fun deleteRecipe(recipeEntity: RecipeEntity)
    val allRecipes : LiveData<List<RecipeEntity>>
}