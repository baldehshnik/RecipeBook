package com.firstapplication.recipebook.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.firstapplication.recipebook.data.interfaces.RecipeRepository
import javax.inject.Singleton

class RecipeAddingViewModel(application: Application, private val repository: RecipeRepository) :
    AndroidViewModel(application) {

    private val ingredientsMap = mutableMapOf<String, String>()

    private val _ingredients = MutableLiveData(ingredientsMap)
    val ingredients: MutableLiveData<MutableMap<String, String>> get() = _ingredients

    fun addNewIngredient(key: String, value: String) {
        ingredientsMap[key] = value
        _ingredients.value = ingredientsMap
    }
}