package com.firstapplication.recipebook.ui.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.firstapplication.recipebook.data.interfaces.RecipeRepository
import com.firstapplication.recipebook.extensions.migrateFromRecipeModelToRecipeEntity
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

    fun setCurrentRecipeIngredients(ingredients: Map<String, String>) {
        ingredientsMap.putAll(ingredients)
        _ingredients.value = ingredientsMap
    }

    private var recipeCategory = "Горячие блюда"

    fun setNewRecipeCategory(categoryName: String) {
        recipeCategory = categoryName
    }

    fun createRecipe(title: String, recipeInfo: String, time: String) =
        insertRecipeToDB(
            RecipeModel(
                title = title,
                recipeInfo = recipeInfo,
                time = time,
                ingredients = ingredientsMap,
                category = recipeCategory
            )
        )


    fun updateRecipeInDB(
        recipeModel: RecipeModel, title: String, recipeInfo: String,
        time: String
    ) {

        recipeModel.title = title
        recipeModel.recipeInfo = recipeInfo
        recipeModel.time = time
        recipeModel.ingredients = ingredientsMap
        recipeModel.category = recipeCategory

        viewModelScope.launch(Dispatchers.IO) {
            repository.updateRecipe(recipeModel.migrateFromRecipeModelToRecipeEntity())
        }
    }

    private fun insertRecipeToDB(recipeModel: RecipeModel) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertRecipe(recipeModel.migrateFromRecipeModelToRecipeEntity())
    }

    fun getCookingTime(hours: String, minutes: String): String {
        var resTime = ""

        if (hours != "0") resTime = "$hours часов"
        if (minutes != "0") resTime += " $minutes минут"

        return resTime.trim()
    }

}