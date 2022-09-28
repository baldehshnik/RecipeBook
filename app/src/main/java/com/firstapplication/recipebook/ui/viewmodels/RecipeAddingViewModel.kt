package com.firstapplication.recipebook.ui.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.firstapplication.recipebook.data.interfaces.RecipeRepository
import com.firstapplication.recipebook.data.models.RecipeEntity
import com.firstapplication.recipebook.extensions.migrateFromRecipeEntityToRecipeModel
import com.firstapplication.recipebook.extensions.migrateFromRecipeModelToRecipeEntity
import com.firstapplication.recipebook.sealed.Error
import com.firstapplication.recipebook.ui.models.RecipeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecipeAddingViewModel(application: Application, private val repository: RecipeRepository) :
    AndroidViewModel(application) {

    private val ingredientsMap = mutableMapOf<String, String>()

    private val _ingredients = MutableLiveData(ingredientsMap)
    val ingredients: LiveData<MutableMap<String, String>> get() = _ingredients

    fun addNewIngredient(key: String, value: String) {
        ingredientsMap[key] = value
        _ingredients.value = ingredientsMap
    }

    fun deleteIngredient(key: String) {
        ingredientsMap.remove(key)
        _ingredients.value = ingredientsMap
    }

    private var recipeCategory = "Горячее блюдо"

    fun setNewRecipeCategory(categoryName: String) {
        recipeCategory = categoryName
    }

    fun createRecipe(title: String, recipeInfo: String, time: Double, timeType: String) =
        insertRecipeToDB(
            RecipeModel(
                title = title,
                recipeInfo = recipeInfo,
                cookingTime = time,
                timeType = timeType,
                ingredients = ingredientsMap,
                category = recipeCategory
            )
        )

    private fun insertRecipeToDB(recipeModel: RecipeModel) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertRecipe(recipeModel.migrateFromRecipeModelToRecipeEntity())
    }

    fun checkTime(time: String): Error = try {
        time.toDouble()
        Error.CorrectResult
    } catch (e: Exception) {
        Error.ErrorResult
    }

}