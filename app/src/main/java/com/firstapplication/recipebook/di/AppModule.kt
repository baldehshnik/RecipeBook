package com.firstapplication.recipebook.di

import android.content.Context
import androidx.room.Room
import com.firstapplication.recipebook.data.interfaces.RecipeDao
import com.firstapplication.recipebook.data.local.RecipeBookDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object AppModule {

    @Singleton
    @Provides
    fun provideRecipeBookDatabase(context: Context): RecipeBookDatabase {
        return Room.databaseBuilder(
            context,
            RecipeBookDatabase::class.java,
            "recipe_database"
        )
            .build()
    }

    @Provides
    fun provideRecipeDao(recipeBookDatabase: RecipeBookDatabase): RecipeDao {
        return recipeBookDatabase.recipeDatabase
    }

}