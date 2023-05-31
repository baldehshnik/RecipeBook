package com.firstapplication.recipebook.ui.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.firstapplication.recipebook.data.interfaces.RecipeRepository
import com.firstapplication.recipebook.domain.usecase.GetDishCategoriesUseCase
import com.firstapplication.recipebook.ui.viewmodels.HomeViewModel
import com.firstapplication.recipebook.utils.AppDispatchers
import javax.inject.Inject

class HomeViewModelFactory @Inject constructor(
    private val repository: RecipeRepository,
    private val dispatchers: AppDispatchers,
    private val getDishCategoriesUseCase: GetDishCategoriesUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(repository, dispatchers, getDishCategoriesUseCase) as T
        }
        throw IllegalArgumentException("View model not found")
    }
}