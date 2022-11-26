package com.firstapplication.recipebook.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.firstapplication.recipebook.data.interfaces.RecipeDao
import com.firstapplication.recipebook.data.models.RecipeEntity

@Database(entities = [RecipeEntity::class], version = 1, exportSchema = false)
abstract class RecipeBookDatabase : RoomDatabase() {
    abstract val recipeDatabase: RecipeDao

    companion object {
        const val DATABASE_NAME = "recipe_database"
    }
}