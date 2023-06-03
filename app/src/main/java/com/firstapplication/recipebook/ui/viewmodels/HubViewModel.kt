package com.firstapplication.recipebook.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.firstapplication.recipebook.domain.usecase.UpdateRecipeInLocalDatabaseUseCase
import com.firstapplication.recipebook.ui.models.RecipeModel
import com.firstapplication.recipebook.utils.AppDispatchers
import kotlinx.coroutines.launch

class HubViewModel(
    private val dispatchers: AppDispatchers,
    private val updateRecipeInLocalDatabaseUseCase: UpdateRecipeInLocalDatabaseUseCase
) : BaseViewModel() {

//    private var observeSavedRecipes = repository.readAllSavedRecipes("")

    private val _savedRecipeList = MutableLiveData<List<RecipeModel>>()
    val savedRecipeList: LiveData<List<RecipeModel>>
        get() = _savedRecipeList

//    private val savedRecipeListObserver = Observer<List<RecipeEntity>> { entitiesList ->
//        setReadRecipes(entitiesList)
//    }
//
//    private val savedRecipeModelsListObserver = Observer<List<RecipeModel>> { models ->
//        _savedRecipeList.value = models
//    }

//    private val recipeSavedModels = MutableLiveData<List<RecipeModel>>()

//    private fun setReadRecipes(entitiesList: List<RecipeEntity>) =
//        viewModelScope.launch {
//            recipeSavedModels.value = this@HubViewModel.getMigratedToRecipeModelList(
//                entitiesList = entitiesList
//            )
//        }

    fun updateRecipeInDB(recipeModel: RecipeModel) {
        viewModelScope.launch(dispatchers.ioDispatcher) {
            updateRecipeInLocalDatabaseUseCase(recipeModel)
        }
    }

//    fun setObserve(string: String) {
//        removeObserve()
//        observeSavedRecipes = repository.readSavedRecipesMatchFormat(string)
//        observeSavedRecipes.observeForever(savedRecipeListObserver)
//    }
//
//    private fun removeObserve() {
//        observeSavedRecipes.removeObserver(savedRecipeListObserver)
//        _savedRecipeList.value = listOf()
//    }
//
//    init {
//        observeSavedRecipes.observeForever(savedRecipeListObserver)
//        recipeSavedModels.observeForever(savedRecipeModelsListObserver)
//    }
//
//    override fun onCleared() {
//        observeSavedRecipes.removeObserver(savedRecipeListObserver)
//        recipeSavedModels.removeObserver(savedRecipeModelsListObserver)
//        super.onCleared()
//    }

}