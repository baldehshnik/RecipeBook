package com.firstapplication.recipebook.domain.usecase

import com.firstapplication.recipebook.data.interfaces.RecipeRepository
import com.firstapplication.recipebook.di.IODispatcher
import com.firstapplication.recipebook.ui.models.RecipeModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteRecipeFromLocalDatabaseUseCase @Inject constructor(
    private val repository: RecipeRepository,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(recipeModel: RecipeModel) = withContext(ioDispatcher) {
        repository.deleteRecipe(recipeModel.toRecipeEntity())
    }
}