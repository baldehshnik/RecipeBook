package com.firstapplication.recipebook.di

import com.firstapplication.recipebook.data.interfaces.RecipeRepository
import com.firstapplication.recipebook.data.repositories.RecipeRepositoryImpl
import com.firstapplication.recipebook.ui.interfacies.OnEditTextFocusChangeListener
import com.firstapplication.recipebook.ui.listeners.OnEditTextFocusChangeListenerImpl
import dagger.Binds
import dagger.Module

@Module
interface AppBindModule {

    @Binds
    fun bindRecipeRepositoryImplToRecipeRepository(
        recipeRepositoryImpl: RecipeRepositoryImpl
    ): RecipeRepository

}