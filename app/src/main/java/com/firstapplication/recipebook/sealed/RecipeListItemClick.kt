package com.firstapplication.recipebook.sealed

sealed class RecipeListItemClick {
    object OnMarkerClick : RecipeListItemClick()
    object OnFullItemClick : RecipeListItemClick()
    object OnItemClickInDeleteMode: RecipeListItemClick()
}