package com.firstapplication.recipebook.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.firstapplication.recipebook.domain.usecase.InsertRecipeInLocalDatabaseUseCase
import com.firstapplication.recipebook.domain.usecase.UpdateRecipeInLocalDatabaseUseCase
import com.firstapplication.recipebook.enums.HourFormats
import com.firstapplication.recipebook.enums.MinuteFormats
import com.firstapplication.recipebook.ui.models.RecipeModel
import com.firstapplication.recipebook.utils.AppDispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.Map
import kotlin.collections.MutableMap
import kotlin.collections.mutableMapOf
import kotlin.collections.set
import kotlin.collections.toList
import kotlin.collections.toMap

class RecipeAddingViewModel(
    private val dispatchers: AppDispatchers,
    private val updateRecipeInLocalDatabaseUseCase: UpdateRecipeInLocalDatabaseUseCase,
    private val insertRecipeInLocalDatabaseUseCase: InsertRecipeInLocalDatabaseUseCase
) : BaseViewModel() {

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

    fun changeIngredientsPosition(fromPosition: Int, toPosition: Int) {
        val ingredientsList = ingredientsMap.toList()
        Collections.swap(ingredientsList, fromPosition, toPosition)
        ingredientsMap.clear()
        ingredientsMap.putAll(ingredientsList.toMap())
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

        viewModelScope.launch(dispatchers.ioDispatcher) {
            updateRecipeInLocalDatabaseUseCase(recipeModel)
        }
    }

    private fun insertRecipeToDB(recipeModel: RecipeModel) {
        viewModelScope.launch(dispatchers.ioDispatcher) {
            insertRecipeInLocalDatabaseUseCase(recipeModel)
        }
    }

    fun getCookingTime(hours: String, minutes: String): String {
        var resTime = ""

        if (hours != "0") resTime = getHoursWithFormat(hours)
        if (minutes != "0") resTime += " ${getMinutesWithFormat(minutes)}"

        return resTime.trim()
    }

    private fun getMinutesWithFormat(minutesString: String): String {
        val minutes = minutesString.toInt()

        val minutesFormat = when {
            minutes in 5..20 -> MinuteFormats.OTHER.minute
            minutes % 10 == 1 -> MinuteFormats.ONE_END.minute
            minutes % 10 in 2..4 -> MinuteFormats.FROM_TWO_TO_FOUR.minute
            else -> MinuteFormats.OTHER.minute
        }

        return "$minutes $minutesFormat"
    }

    private fun getHoursWithFormat(hoursString: String): String {
        val hours = hoursString.toInt()

        val hoursFormat = when {
            hours in 5..20 -> HourFormats.OTHER.hour
            hours % 10 == 1 -> HourFormats.ONE_END.hour
            hours % 10 in 2..4 -> HourFormats.FROM_TWO_TO_FOUR.hour
            else -> HourFormats.OTHER.hour
        }

        return "$hours $hoursFormat"
    }
}