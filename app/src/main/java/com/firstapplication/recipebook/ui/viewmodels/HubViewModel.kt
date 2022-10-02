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
import com.firstapplication.recipebook.extensions.migrateFromRecipeModelToRecipeEntity
import com.firstapplication.recipebook.ui.models.RecipeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HubViewModel(application: Application, private val repository: RecipeRepository) :
    AndroidViewModel(application) {

    private val _savedRecipeList = MutableLiveData<List<RecipeModel>>()
    val savedRecipeList: LiveData<List<RecipeModel>>
        get() = _savedRecipeList

    private val savedRecipeListObserver = Observer<List<RecipeEntity>> { entitiesList ->
        setReadRecipes(entitiesList = entitiesList)
    }

    private val savedRecipeModelsListObserver = Observer<List<RecipeModel>> { models ->
        _savedRecipeList.value = models
    }

    private val recipeSavedModels = MutableLiveData<List<RecipeModel>>()

    private fun setReadRecipes(entitiesList: List<RecipeEntity>) =
        viewModelScope.launch {
            recipeSavedModels.value = this@HubViewModel.getMigratedToRecipeModelList(
                entitiesList = entitiesList
            )
        }

    fun updateRecipeInDB(recipeModel: RecipeModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateRecipe(
                recipeEntity = recipeModel.migrateFromRecipeModelToRecipeEntity()
            )
        }
    }

    init {
        repository.allSavedRecipes("").observeForever(savedRecipeListObserver)
        recipeSavedModels.observeForever(savedRecipeModelsListObserver)
    }

    override fun onCleared() {
        repository.allSavedRecipes("").removeObserver(savedRecipeListObserver)
        recipeSavedModels.removeObserver(savedRecipeModelsListObserver)
        super.onCleared()
    }

}