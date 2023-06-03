package com.firstapplication.recipebook.data.interfaces

import com.firstapplication.recipebook.data.models.RecipeEntity
import com.firstapplication.recipebook.sealed.Response

interface RecipeRepository {
    suspend fun updateRecipe(recipeEntity: RecipeEntity)
    suspend fun insertRecipe(recipeEntity: RecipeEntity)
    suspend fun deleteRecipe(recipeEntity: RecipeEntity)

    fun readAllRecipesInCategory(category: String) : List<RecipeEntity>

    fun readAllSavedRecipes(category: String) : List<RecipeEntity>

    fun readRecipesMatchFormat(format: String) : List<RecipeEntity>

    fun readSavedRecipesMatchFormat(format: String) : List<RecipeEntity>

    fun readRecipesInDescendingOrder(): List<RecipeEntity>

    suspend fun readAllCategories(): Response
}