package com.firstapplication.recipebook.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.firstapplication.recipebook.data.interfaces.RecipeRepository
import com.firstapplication.recipebook.data.repositories.RecipeRepositoryImpl

class HomeViewModel(application: Application, private val repository: RecipeRepository) :
    AndroidViewModel(application) {
}