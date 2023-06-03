package com.firstapplication.recipebook.ui.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.firstapplication.recipebook.domain.usecase.InsertRecipeInLocalDatabaseUseCase
import com.firstapplication.recipebook.domain.usecase.UpdateRecipeInLocalDatabaseUseCase
import com.firstapplication.recipebook.ui.viewmodels.RecipeAddingViewModel
import com.firstapplication.recipebook.utils.AppDispatchers
import javax.inject.Inject

class RecipeAddingViewModelFactory @Inject constructor(
    private val dispatchers: AppDispatchers,
    private val updateRecipeInLocalDatabaseUseCase: UpdateRecipeInLocalDatabaseUseCase,
    private val insertRecipeInLocalDatabaseUseCase: InsertRecipeInLocalDatabaseUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeAddingViewModel::class.java)) {
            return RecipeAddingViewModel(
                dispatchers,
                updateRecipeInLocalDatabaseUseCase,
                insertRecipeInLocalDatabaseUseCase
            ) as T
        }
        throw IllegalArgumentException("View model not found")
    }
}