package com.firstapplication.recipebook.data.repositories

import androidx.lifecycle.LiveData
import com.firstapplication.recipebook.data.interfaces.RecipeDao
import com.firstapplication.recipebook.data.interfaces.RecipeRepository
import com.firstapplication.recipebook.data.models.RecipeEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeRepositoryImpl @Inject constructor(private val database: RecipeDao) : RecipeRepository {

    override suspend fun updateRecipe(recipeEntity: RecipeEntity) {
        database.update(recipeEntity)
    }

    override suspend fun insertRecipe(recipeEntity: RecipeEntity) {
        database.insert(recipeEntity)
    }

    override suspend fun deleteRecipe(recipeEntity: RecipeEntity) {
        database.delete(recipeEntity)
    }

    override fun readAllRecipesInCategory(category: String): LiveData<List<RecipeEntity>> {
        return if (category == "") database.readAllRecipesReversed()
        else database.readAllRecipesInCategoryReversed(category)
    }

    override fun readAllSavedRecipes(category: String): LiveData<List<RecipeEntity>> {
        return if (category == "") database.readAllSavedRecipes()
        else database.readAllSavedRecipesInCategory(category)
    }

    override fun readRecipesMatchFormat(format: String): LiveData<List<RecipeEntity>> {
        return database.readAllRecipesThatMatchFormat(getFormat(string = format))
    }

    override fun readSavedRecipesMatchFormat(format: String): LiveData<List<RecipeEntity>> {
        return if (format == "") database.readAllSavedRecipes()
        else database.readAllSavedRecipesThatMatchFormat(getFormat(string = format))
    }

    private fun getFormat(string: String): String = "%$string%"
}