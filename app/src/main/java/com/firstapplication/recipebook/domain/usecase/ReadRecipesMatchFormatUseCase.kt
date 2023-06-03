package com.firstapplication.recipebook.domain.usecase

import com.firstapplication.recipebook.data.interfaces.RecipeRepository
import com.firstapplication.recipebook.di.DefaultDispatcher
import com.firstapplication.recipebook.ui.models.RecipeModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReadRecipesMatchFormatUseCase @Inject constructor(
    private val repository: RecipeRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(format: String): List<RecipeModel> {
        return withContext(defaultDispatcher) {
            val data = repository.readRecipesMatchFormat(format)
            return@withContext data.map { it.toRecipeModel() }
        }
    }
}