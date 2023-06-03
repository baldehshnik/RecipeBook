package com.firstapplication.recipebook.ui.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.firstapplication.recipebook.domain.usecase.UpdateRecipeInLocalDatabaseUseCase
import com.firstapplication.recipebook.ui.viewmodels.RecipeSearchingViewModel
import com.firstapplication.recipebook.utils.AppDispatchers
import javax.inject.Inject

class RecipeSearchingViewModelFactory @Inject constructor(
    private val dispatchers: AppDispatchers,
    private val updateRecipeInLocalDatabaseUseCase: UpdateRecipeInLocalDatabaseUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeSearchingViewModel::class.java)) {
            return RecipeSearchingViewModel(
                dispatchers,
                updateRecipeInLocalDatabaseUseCase
            ) as T
        }
        throw IllegalArgumentException("View model not found")
    }
}