package com.firstapplication.recipebook.data.models

import com.firstapplication.recipebook.ui.models.DishCategoryModel

data class DishCategoryEntity(
    val id: Int,
    val name: String,
    val imgSrc: String
) {
    fun toDishCategoryModel(): DishCategoryModel {
        return DishCategoryModel(id, name, imgSrc)
    }
}