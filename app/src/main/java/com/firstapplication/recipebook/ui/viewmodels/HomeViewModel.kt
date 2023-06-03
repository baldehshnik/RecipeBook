package com.firstapplication.recipebook.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.firstapplication.recipebook.R
import com.firstapplication.recipebook.domain.usecase.DeleteRecipeFromLocalDatabaseUseCase
import com.firstapplication.recipebook.domain.usecase.GetDishCategoriesUseCase
import com.firstapplication.recipebook.domain.usecase.UpdateRecipeInLocalDatabaseUseCase
import com.firstapplication.recipebook.sealed.ErrorResponse
import com.firstapplication.recipebook.sealed.ProgressResponse
import com.firstapplication.recipebook.ui.interfacies.Navigator
import com.firstapplication.recipebook.ui.models.DishCategoryModel
import com.firstapplication.recipebook.ui.models.RecipeModel
import com.firstapplication.recipebook.utils.AppDispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val dispatchers: AppDispatchers,
    private val getDishCategoriesUseCase: GetDishCategoriesUseCase,
    private val deleteRecipeFromLocalDatabaseUseCase: DeleteRecipeFromLocalDatabaseUseCase,
    private val updateRecipeInLocalDatabaseUseCase: UpdateRecipeInLocalDatabaseUseCase,
    private val navigator: Navigator
) : BaseViewModel() {

    private val selectedRecipes = mutableListOf<RecipeModel>()

    private val _categories = MutableLiveData<List<DishCategoryModel>>()
    val categories: LiveData<List<DishCategoryModel>> get() = _categories

    fun readCategories() {
        viewModelScope.launch(dispatchers.defaultDispatcher) {
            val result = getDishCategoriesUseCase()
            withContext(dispatchers.mainDispatcher) {
                when(result.second) {
                    is ErrorResponse -> { navigator.toast(R.string.error) }
                    is ProgressResponse -> { }
                    else -> { _categories.value = result.first!! }
                }
            }
        }
    }








//    private var observableRecipes = repository.readAllRecipesInCategory("")

    fun addNewSelectedRecipe(recipe: RecipeModel) {
        selectedRecipes.add(recipe)
        _selectedRecipesCount.value = selectedRecipes.size
    }

    fun deleteSelectedRecipe(recipe: RecipeModel) {
        selectedRecipes.remove(recipe)
        _selectedRecipesCount.value = selectedRecipes.size
    }

    fun clearSelectedRecipes() {
        selectedRecipes.clear()
        _selectedRecipesCount.value = selectedRecipes.size
    }

    fun deleteSelectedRecipesFromDB() {
        viewModelScope.launch(dispatchers.defaultDispatcher) {
            selectedRecipes.forEach { item ->
                deleteRecipeFromLocalDatabaseUseCase(item)
            }
            selectedRecipes.clear()
        }
    }

    private val _selectedRecipesCount = MutableLiveData(selectedRecipes.size)
    val selectedRecipesCount: LiveData<Int> get() = _selectedRecipesCount

    private val _recipesList = MutableLiveData<List<RecipeModel>>()
    val recipesList: LiveData<List<RecipeModel>> get() = _recipesList

//    private val recipeListObserver = Observer<List<RecipeEntity>> { entitiesList ->
//        setReadRecipes(entitiesList)
//    }
//
//    private val recipeModelsListObserver = Observer<List<RecipeModel>> { models ->
//        _recipesList.value = models
//    }

//    private val recipeModels = MutableLiveData<List<RecipeModel>>()

//    private fun setReadRecipes(entitiesList: List<RecipeEntity>) {
//        viewModelScope.launch {
//            recipeModels.value = this@HomeViewModel.getMigratedToRecipeModelList(entitiesList)
//        }
//    }

    fun updateRecipeInDB(recipeModel: RecipeModel) = viewModelScope.launch(dispatchers.ioDispatcher) {
        updateRecipeInLocalDatabaseUseCase(recipeModel)
    }

//    fun changeRecipeList(category: String) {
//        observableRecipes.removeObserver(recipeListObserver)
//        observableRecipes = repository.readAllRecipesInCategory(category)
//        observableRecipes.observeForever(recipeListObserver)
//    }
//
//    init {
//        observableRecipes.observeForever(recipeListObserver)
//        recipeModels.observeForever(recipeModelsListObserver)
//    }
//
//    override fun onCleared() {
//        observableRecipes.removeObserver(recipeListObserver)
//        recipeModels.removeObserver(recipeModelsListObserver)
//        super.onCleared()
//    }
}