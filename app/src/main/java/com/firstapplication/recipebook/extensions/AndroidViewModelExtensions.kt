package com.firstapplication.recipebook.extensions

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.firstapplication.recipebook.data.interfaces.RecipeRepository
import com.firstapplication.recipebook.data.models.RecipeEntity
import com.firstapplication.recipebook.ui.models.RecipeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

suspend fun AndroidViewModel.getMigratedToRecipeModelList(
    entitiesList: List<RecipeEntity>
): List<RecipeModel> {

    val list = mutableListOf<RecipeModel>()

    val coroutineResult = viewModelScope.async {
        entitiesList.forEach { item ->
            list.add(item.migrateFromRecipeEntityToRecipeModel())
        }
    }

    coroutineResult.await()

    return list
}

fun AndroidViewModel.updateRecipeInDB(recipeModel: RecipeModel, repository: RecipeRepository) {
    viewModelScope.launch(Dispatchers.IO) {
        repository.updateRecipe(
            recipeEntity = recipeModel.migrateFromRecipeModelToRecipeEntity()
        )
    }
}