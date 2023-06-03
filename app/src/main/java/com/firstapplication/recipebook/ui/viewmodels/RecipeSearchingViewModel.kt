package com.firstapplication.recipebook.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.firstapplication.recipebook.domain.usecase.UpdateRecipeInLocalDatabaseUseCase
import com.firstapplication.recipebook.ui.models.RecipeModel
import com.firstapplication.recipebook.utils.AppDispatchers
import kotlinx.coroutines.launch

class RecipeSearchingViewModel(
    private val dispatchers: AppDispatchers,
    private val updateRecipeInLocalDatabaseUseCase: UpdateRecipeInLocalDatabaseUseCase
) : BaseViewModel() {

//    private var observableRecipes = repository.readRecipesMatchFormat("")

    private val _searchRecipesList = MutableLiveData<List<RecipeModel>>()
    val searchRecipesList: LiveData<List<RecipeModel>>
        get() = _searchRecipesList

//    private val searchRecipeListObserver = Observer<List<RecipeEntity>> { entitiesList ->
//        setReadRecipes(entitiesList = entitiesList)
//    }

    private val recipeModelsListObserver = Observer<List<RecipeModel>> { models ->
        _searchRecipesList.value = models
    }

    private val recipeModels = MutableLiveData<List<RecipeModel>>()

//    private fun setReadRecipes(entitiesList: List<RecipeEntity>) =
//        viewModelScope.launch {
//            recipeModels.value = this@RecipeSearchingViewModel.getMigratedToRecipeModelList(
//                entitiesList = entitiesList
//            )
//        }

//    fun setObserve(string: String) {
//        removeObserve()
//        observableRecipes = repository.readRecipesMatchFormat(string)
//        observableRecipes.observeForever(searchRecipeListObserver)
//    }
//
//    fun removeObserve() {
//        observableRecipes.removeObserver(searchRecipeListObserver)
//        observableRecipes = repository.readRecipesMatchFormat("")
//        _searchRecipesList.value = listOf()
//    }

    fun updateRecipeInDB(recipeModel: RecipeModel) {
        viewModelScope.launch(dispatchers.ioDispatcher) {
            updateRecipeInLocalDatabaseUseCase(recipeModel)
        }
    }

    init {
        recipeModels.observeForever(recipeModelsListObserver)
    }

//    override fun onCleared() {
//        observableRecipes.removeObserver(searchRecipeListObserver)
//        recipeModels.removeObserver(recipeModelsListObserver)
//        super.onCleared()
//    }

}