package com.firstapplication.recipebook.extensions

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.firstapplication.recipebook.data.models.RecipeEntity
import com.firstapplication.recipebook.ui.models.RecipeModel
import kotlinx.coroutines.async

suspend fun AndroidViewModel.getMigratedToRecipeModelList(
    entitiesList: List<RecipeEntity>
): List<RecipeModel> {

    val list = mutableListOf<RecipeModel>()

    val coroutineResult = this.viewModelScope.async {
        entitiesList.forEach { item ->
            list.add(item.migrateFromRecipeEntityToRecipeModel())
        }
    }

    coroutineResult.await()

    return list
}