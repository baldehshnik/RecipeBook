package com.firstapplication.recipebook.domain.usecase

import com.firstapplication.recipebook.data.interfaces.RecipeRepository
import com.firstapplication.recipebook.data.models.DishCategoryEntity
import com.firstapplication.recipebook.di.DefaultDispatcher
import com.firstapplication.recipebook.sealed.CorrectResponse
import com.firstapplication.recipebook.sealed.ErrorResponse
import com.firstapplication.recipebook.sealed.Response
import com.firstapplication.recipebook.ui.models.DishCategoryModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetDishCategoriesUseCase @Inject constructor(
    private val repository: RecipeRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(): Pair<List<DishCategoryModel>?, Response> {
        return withContext(defaultDispatcher) {
            val answer = repository.readAllCategories()
            if (answer !is CorrectResponse<*>) return@withContext (null to answer)

            return@withContext if (answer.listValue != null) {
                val data: List<DishCategoryModel> = answer.listValue!!.map {
                    if (it !is DishCategoryEntity) return@withContext (null to ErrorResponse())
                    it.toDishCategoryModel()
                }
                (data to CorrectResponse<Any>())
            } else {
                (null to ErrorResponse())
            }
        }
    }
}