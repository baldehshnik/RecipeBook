package com.firstapplication.recipebook.data.repositories

import androidx.lifecycle.LiveData
import com.firstapplication.recipebook.data.interfaces.RecipeDao
import com.firstapplication.recipebook.data.interfaces.RecipeRepository
import com.firstapplication.recipebook.data.models.RecipeEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeRepositoryImpl @Inject constructor(private val database: RecipeDao) : RecipeRepository {

    override suspend fun insertRecipe(recipeEntity: RecipeEntity) {
        database.insert(recipeEntity = recipeEntity)
    }

    override suspend fun deleteRecipe(recipeEntity: RecipeEntity) {
        database.delete(recipeEntity)
    }

    override val allRecipes: LiveData<List<RecipeEntity>> = database.readAllRecipes()
}