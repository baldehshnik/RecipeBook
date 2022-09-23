package com.firstapplication.recipebook.data.interfaces

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.firstapplication.recipebook.data.models.RecipeEntity

@Dao
interface RecipeDao {

    @Insert
    suspend fun insert(recipeEntity: RecipeEntity)

    @Query("SELECT * FROM recipes")
    fun readAllRecipes(): LiveData<List<RecipeEntity>>

}