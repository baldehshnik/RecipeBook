package com.firstapplication.recipebook.ui.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.firstapplication.recipebook.data.interfaces.RecipeRepository
import com.firstapplication.recipebook.ui.viewmodels.HomeViewModel
import com.firstapplication.recipebook.ui.viewmodels.HubViewModel
import com.firstapplication.recipebook.ui.viewmodels.RecipeAddingViewModel
import com.firstapplication.recipebook.ui.viewmodels.RecipeSearchingViewModel
import com.firstapplication.recipebook.utils.AppDispatchers
import javax.inject.Inject

class OnlyRecipeRepositoryViewModelFactory @Inject constructor(
    private val repository: RecipeRepository,
//    private val dispatchers: AppDispatchers
) : ViewModelProvider.Factory {

//    @AssistedFactory
//    interface Factory {
//        fun create(@Assisted(ASSISTED_APPLICATION) application: Application): OnlyRecipeRepositoryViewModelFactory
//    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
//        modelClass.isAssignableFrom(HomeViewModel::class.java) -> getHomeViewModelAsT()
        modelClass.isAssignableFrom(RecipeAddingViewModel::class.java) -> getRecipeAddingViewModelAsT()
        modelClass.isAssignableFrom(HubViewModel::class.java) -> getHubViewModeAsT()
        modelClass.isAssignableFrom(RecipeSearchingViewModel::class.java) -> getRecipeSearchingViewModelAsT()
        else -> throw IllegalArgumentException("ViewModel not found")
    }

//    @Suppress("UNCHECKED_CAST")
//    private fun <T : ViewModel> getHomeViewModelAsT(): T =
//        HomeViewModel(repository, dispatchers) as T

    @Suppress("UNCHECKED_CAST")
    private fun <T : ViewModel> getRecipeAddingViewModelAsT(): T =
        RecipeAddingViewModel(repository = repository) as T

    @Suppress("UNCHECKED_CAST")
    private fun <T : ViewModel> getHubViewModeAsT(): T =
        HubViewModel(repository = repository) as T

    @Suppress("UNCHECKED_CAST")
    private fun <T : ViewModel> getRecipeSearchingViewModelAsT(): T =
        RecipeSearchingViewModel(repository = repository) as T

//    companion object {
//        const val ASSISTED_APPLICATION = "application"
//    }
}