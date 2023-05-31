package com.firstapplication.recipebook.di

import com.firstapplication.recipebook.data.interfaces.DatabaseHelper
import com.firstapplication.recipebook.data.interfaces.RecipeRepository
import com.firstapplication.recipebook.data.local.DatabaseHelperImpl
import com.firstapplication.recipebook.data.repositories.RecipeRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
interface AppBindModule {

    @Binds
    fun bindRecipeRepositoryImplToRecipeRepository(
        recipeRepositoryImpl: RecipeRepositoryImpl
    ): RecipeRepository

    @Binds
    fun bindDatabaseHelperImplToDatabaseHelper(
        databaseHelperImpl: DatabaseHelperImpl
    ): DatabaseHelper

}