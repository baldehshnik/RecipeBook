package com.firstapplication.recipebook.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.firstapplication.recipebook.data.interfaces.RecipeRepository
import com.firstapplication.recipebook.data.models.RecipeEntity
import com.firstapplication.recipebook.extensions.getMigratedToRecipeModelList
import com.firstapplication.recipebook.ui.models.RecipeModel
import kotlinx.coroutines.launch

class RecipeSearchingViewModel(
    application: Application, private val repository: RecipeRepository
) : AndroidViewModel(application) {

    private var observeFormat = ""

    private val _searchRecipesList = MutableLiveData<List<RecipeModel>>()
    val searchRecipesList: LiveData<List<RecipeModel>>
        get() = _searchRecipesList

    private val searchRecipeListObserver = Observer<List<RecipeEntity>> { entitiesList ->
        setReadRecipes(entitiesList = entitiesList)
    }

    private val recipeModelsListObserver = Observer<List<RecipeModel>> { models ->
        _searchRecipesList.value = models
    }

    private val recipeModels = MutableLiveData<List<RecipeModel>>()

    private fun setReadRecipes(entitiesList: List<RecipeEntity>) =
        viewModelScope.launch {
            recipeModels.value = this@RecipeSearchingViewModel.getMigratedToRecipeModelList(
                entitiesList = entitiesList
            )
        }

    fun setObserve(string: String) {
        removeObserve()
        observeFormat = string
        repository.readRecipesMatchFormat(string).observeForever(searchRecipeListObserver)
    }

    fun removeObserve() {
        repository.readRecipesMatchFormat(observeFormat).removeObserver(searchRecipeListObserver)
        observeFormat = ""
        _searchRecipesList.value = listOf()
    }

    init {
        recipeModels.observeForever(recipeModelsListObserver)
    }

    override fun onCleared() {
        repository.readRecipesMatchFormat(observeFormat).removeObserver(searchRecipeListObserver)
        recipeModels.removeObserver(recipeModelsListObserver)
        super.onCleared()
    }

}