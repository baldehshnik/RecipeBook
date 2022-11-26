package com.firstapplication.recipebook.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.firstapplication.recipebook.data.interfaces.RecipeRepository
import com.firstapplication.recipebook.data.models.RecipeEntity
import com.firstapplication.recipebook.ui.models.RecipeModel
import kotlinx.coroutines.launch

class RecipeSearchingViewModel(
    application: Application, private val repository: RecipeRepository
) : BasicAndroidViewModel(application = application) {

    private var observableRecipes = repository.readRecipesMatchFormat("")

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
        observableRecipes = repository.readRecipesMatchFormat(string)
        observableRecipes.observeForever(searchRecipeListObserver)
    }

    fun removeObserve() {
        observableRecipes.removeObserver(searchRecipeListObserver)
        observableRecipes = repository.readRecipesMatchFormat("")
        _searchRecipesList.value = listOf()
    }

    fun updateRecipeInDB(recipeModel: RecipeModel) =
        updateRecipeInDB(recipeModel = recipeModel, repository = repository)

    init {
        recipeModels.observeForever(recipeModelsListObserver)
    }

    override fun onCleared() {
        observableRecipes.removeObserver(searchRecipeListObserver)
        recipeModels.removeObserver(recipeModelsListObserver)
        super.onCleared()
    }

}