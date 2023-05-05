package com.firstapplication.recipebook.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.firstapplication.recipebook.data.interfaces.RecipeRepository
import com.firstapplication.recipebook.data.models.RecipeEntity
import com.firstapplication.recipebook.ui.models.RecipeModel
import kotlinx.coroutines.*

class HomeViewModel(private val repository: RecipeRepository) : BasicViewModel() {

    private var observableRecipes = repository.readAllRecipesInCategory("")

    private val selectedRecipes = mutableListOf<RecipeModel>()

    fun addNewSelectedRecipes(recipeModel: RecipeModel) {
        selectedRecipes.add(recipeModel)
        _selectedRecipesCount.value = selectedRecipes.size
    }

    fun deleteSelectedRecipe(recipeModel: RecipeModel) {
        selectedRecipes.remove(recipeModel)
        _selectedRecipesCount.value = selectedRecipes.size
    }

    fun clearSelectedRecipe() {
        selectedRecipes.clear()
        _selectedRecipesCount.value = selectedRecipes.size
    }

    fun deleteSelectedRecipesFromDB() {
        viewModelScope.launch(Dispatchers.IO) {
            selectedRecipes.forEach { item ->
                repository.deleteRecipe(item.migrateFromRecipeModelToRecipeEntity())
            }

            selectedRecipes.clear()
        }
    }

    private val _selectedRecipesCount = MutableLiveData(selectedRecipes.size)
    val selectedRecipesCount: LiveData<Int> get() = _selectedRecipesCount

    private val _recipesList = MutableLiveData<List<RecipeModel>>()
    val recipesList: LiveData<List<RecipeModel>> get() = _recipesList

    private val recipeListObserver = Observer<List<RecipeEntity>> { entitiesList ->
        setReadRecipes(entitiesList)
    }

    private val recipeModelsListObserver = Observer<List<RecipeModel>> { models ->
        _recipesList.value = models
    }

    private val recipeModels = MutableLiveData<List<RecipeModel>>()

    private fun setReadRecipes(entitiesList: List<RecipeEntity>) {
        viewModelScope.launch {
            recipeModels.value = this@HomeViewModel.getMigratedToRecipeModelList(entitiesList)
        }
    }

    fun updateRecipeInDB(recipeModel: RecipeModel) = updateRecipeInDB(recipeModel, repository)

    fun changeRecipeList(category: String) {
        observableRecipes.removeObserver(recipeListObserver)
        observableRecipes = repository.readAllRecipesInCategory(category)
        observableRecipes.observeForever(recipeListObserver)
    }

    init {
        observableRecipes.observeForever(recipeListObserver)
        recipeModels.observeForever(recipeModelsListObserver)
    }

    override fun onCleared() {
        observableRecipes.removeObserver(recipeListObserver)
        recipeModels.removeObserver(recipeModelsListObserver)
        super.onCleared()
    }
}