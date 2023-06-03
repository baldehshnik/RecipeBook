package com.firstapplication.recipebook.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.firstapplication.recipebook.R
import com.firstapplication.recipebook.databinding.FragmentRecipeInfoBinding
import com.firstapplication.recipebook.extensions.appComponent
import com.firstapplication.recipebook.ui.adapters.IngredientDisplayAdapter
import com.firstapplication.recipebook.ui.models.RecipeModel
import com.firstapplication.recipebook.ui.viewmodels.RecipeInfoViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class RecipeInfoFragment : BaseFragment() {
    private lateinit var binding: FragmentRecipeInfoBinding
    private lateinit var recipe: RecipeModel
    private val recipeArgs: RecipeInfoFragmentArgs by navArgs()

    override val viewModel: RecipeInfoViewModel by viewModels()

    override fun onAttach(context: Context) {
        context.applicationContext.appComponent.inject(this)
        activity?.findViewById<Toolbar>(R.id.toolbar)?.isVisible = false
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavView)?.isVisible = false
        super.onAttach(context)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecipeInfoBinding.inflate(layoutInflater, container, false)

        if (recipeArgs.selectedRecipe == null) dataTransferError()
        else recipe = recipeArgs.selectedRecipe!!

        with(binding) {
            btnOut.setOnClickListener { findNavController().popBackStack() }
            btnEdit.setOnClickListener {
                findNavController().navigate(
                    RecipeInfoFragmentDirections.actionNavRecipeInfoToNavAdding(recipe)
                )
            }

            twTitle.text = recipe.title
            twTime.text = getStringFromRes(R.string.time_display) + recipe.time

            if (recipe.recipeInfo.isEmpty()) twRecipeInfo.isVisible = false
            else twRecipeInfo.text = recipe.recipeInfo

            twCategory.text = getStringFromRes(R.string.category_display) + recipe.category

            val ingredients = recipe.ingredients.toMutableMap()
            if (ingredients.containsKey("")) {
                ingredients.remove("")
            }

            val adapter = IngredientDisplayAdapter().also { rwIngredients.adapter = it }
            adapter.submitList(ingredients.toList())
        }

        return binding.root
    }

    private fun dataTransferError() {
        toast(getStringFromRes(R.string.data_transfer_error))
        findNavController().popBackStack()
    }

    override fun onDestroy() {
        activity?.findViewById<Toolbar>(R.id.toolbar)?.isVisible = true
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavView)?.isVisible = true
        super.onDestroy()
    }
}