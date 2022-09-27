package com.firstapplication.recipebook.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.firstapplication.recipebook.data.interfaces.RecipeRepository
import com.firstapplication.recipebook.sealed.Error
import com.firstapplication.recipebook.ui.models.RecipeModel

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

    fun createRecipe(title: String, recipeInfo: String, time: Double, timeType: String) {
        var newRecipe = RecipeModel(
            title = title,
            recipeInfo = recipeInfo,
            cookingTime = time,
            timeType = timeType,
            ingredients = ingredientsMap,
            category = recipeCategory
        )
    }

    fun checkTime(time: String): Error {
        try {
            time.toDouble()
            return Error.CorrectResult
        } catch (e: Exception) {
            return Error.ErrorResult
        }
    }
}