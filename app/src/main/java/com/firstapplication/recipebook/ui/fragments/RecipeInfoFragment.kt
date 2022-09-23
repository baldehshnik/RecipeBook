package com.firstapplication.recipebook.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.firstapplication.recipebook.R
import com.firstapplication.recipebook.databinding.FragmentRecipeInfoBinding

class RecipeInfoFragment : Fragment(R.layout.fragment_recipe_info) {

    private lateinit var binding: FragmentRecipeInfoBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRecipeInfoBinding.bind(view)
    }

}