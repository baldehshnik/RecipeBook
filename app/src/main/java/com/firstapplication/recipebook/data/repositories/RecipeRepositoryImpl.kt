package com.firstapplication.recipebook.data.repositories

import com.firstapplication.recipebook.data.interfaces.DatabaseHelper
import com.firstapplication.recipebook.data.interfaces.RecipeDao
import com.firstapplication.recipebook.data.interfaces.RecipeRepository
import com.firstapplication.recipebook.data.models.RecipeEntity
import com.firstapplication.recipebook.di.IODispatcher
import com.firstapplication.recipebook.sealed.Response
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeRepositoryImpl @Inject constructor(
    private val recipeDatabase: RecipeDao,
    private val categoryDatabase: DatabaseHelper,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : RecipeRepository {

    override suspend fun readAllCategories(): Response = withContext(ioDispatcher) {
        return@withContext categoryDatabase.readAll()
    }

    override suspend fun updateRecipe(recipeEntity: RecipeEntity) = withContext(ioDispatcher) {
        recipeDatabase.update(recipeEntity)
    }

    override suspend fun insertRecipe(recipeEntity: RecipeEntity) = withContext(ioDispatcher) {
        recipeDatabase.insert(recipeEntity)
    }

    override suspend fun deleteRecipe(recipeEntity: RecipeEntity) = withContext(ioDispatcher) {
        recipeDatabase.delete(recipeEntity)
    }

    override fun readRecipesInDescendingOrder(): List<RecipeEntity> {
        return recipeDatabase.readAllRecipesReversed()
    }

    override fun readAllRecipesInCategory(category: String): List<RecipeEntity> {
        return if (category.isBlank()) recipeDatabase.readAllRecipesReversed()
        else recipeDatabase.readAllRecipesInCategoryReversed(category)
    }

    override fun readAllSavedRecipes(category: String): List<RecipeEntity> {
        return if (category.isBlank()) recipeDatabase.readAllSavedRecipes()
        else recipeDatabase.readAllSavedRecipesInCategory(category)
    }

    override fun readRecipesMatchFormat(format: String): List<RecipeEntity> {
        return recipeDatabase.readAllRecipesThatMatchFormat(getFormat(format))
    }

    override fun readSavedRecipesMatchFormat(format: String): List<RecipeEntity> {
        return if (format.isBlank()) recipeDatabase.readAllSavedRecipes()
        else recipeDatabase.readAllSavedRecipesThatMatchFormat(getFormat(format))
    }

    private fun getFormat(string: String) = "%$string%"
}