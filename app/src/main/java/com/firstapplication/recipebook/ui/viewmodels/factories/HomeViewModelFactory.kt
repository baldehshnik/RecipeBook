package com.firstapplication.recipebook.ui.viewmodels.factories

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.firstapplication.recipebook.domain.usecase.DeleteRecipeFromLocalDatabaseUseCase
import com.firstapplication.recipebook.domain.usecase.GetDishCategoriesUseCase
import com.firstapplication.recipebook.domain.usecase.UpdateRecipeInLocalDatabaseUseCase
import com.firstapplication.recipebook.ui.activities.HomeActivity
import com.firstapplication.recipebook.ui.viewmodels.HomeViewModel
import com.firstapplication.recipebook.utils.AppDispatchers
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class HomeViewModelFactory @AssistedInject constructor(
    private val dispatchers: AppDispatchers,
    private val getDishCategoriesUseCase: GetDishCategoriesUseCase,
    private val deleteRecipeFromLocalDatabaseUseCase: DeleteRecipeFromLocalDatabaseUseCase,
    private val updateRecipeInLocalDatabaseUseCase: UpdateRecipeInLocalDatabaseUseCase,
    @Assisted("fragment") private val fragment: Fragment
) : ViewModelProvider.Factory {

    @AssistedFactory
    interface Factory {
        fun build(@Assisted("fragment") fragment: Fragment): HomeViewModelFactory
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(
                dispatchers,
                getDishCategoriesUseCase,
                deleteRecipeFromLocalDatabaseUseCase,
                updateRecipeInLocalDatabaseUseCase,
                (fragment.activity as HomeActivity).navigator
            ) as T
        }
        throw IllegalArgumentException("View model not found")
    }
}