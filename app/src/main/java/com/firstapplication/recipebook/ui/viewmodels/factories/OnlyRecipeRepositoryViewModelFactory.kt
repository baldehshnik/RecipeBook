package com.firstapplication.recipebook.ui.viewmodels.factories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.firstapplication.recipebook.data.interfaces.RecipeRepository
import com.firstapplication.recipebook.ui.viewmodels.HomeViewModel
import com.firstapplication.recipebook.ui.viewmodels.HubViewModel
import com.firstapplication.recipebook.ui.viewmodels.RecipeAddingViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import java.lang.IllegalArgumentException
import javax.inject.Singleton

class OnlyRecipeRepositoryViewModelFactory @AssistedInject constructor(
    @Assisted("application") private val application: Application,
    private val repository: RecipeRepository
) : ViewModelProvider.AndroidViewModelFactory(application = application) {

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("application") application: Application): OnlyRecipeRepositoryViewModelFactory
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(HomeViewModel::class.java) -> getHomeViewModelAsT()
        modelClass.isAssignableFrom(RecipeAddingViewModel::class.java) -> getRecipeAddingViewModelAsT()
        modelClass.isAssignableFrom(HubViewModel::class.java) -> getHubViewModeAsT()
        else -> throw IllegalArgumentException("ViewModel not found")
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : ViewModel> getHomeViewModelAsT(): T =
        HomeViewModel(application = application, repository = repository) as T

    @Suppress("UNCHECKED_CAST")
    private fun <T : ViewModel> getRecipeAddingViewModelAsT(): T =
        RecipeAddingViewModel(application = application, repository = repository) as T

    @Suppress("UNCHECKED_CAST")
    private fun <T : ViewModel> getHubViewModeAsT(): T =
        HubViewModel(application = application, repository = repository) as T
}