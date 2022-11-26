package com.firstapplication.recipebook.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.firstapplication.recipebook.R
import com.firstapplication.recipebook.databinding.FragmentRecipeInfoBinding
import com.firstapplication.recipebook.ui.adapters.IngredientDisplayAdapter
import com.firstapplication.recipebook.ui.models.RecipeModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class RecipeInfoFragment : BasicFragment() {
    private lateinit var binding: FragmentRecipeInfoBinding
    private lateinit var recipe: RecipeModel

    private val recipeArgs: RecipeInfoFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        activity?.findViewById<Toolbar>(R.id.toolbar)?.visibility = View.GONE
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavView)?.visibility = View.GONE
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

        val adapter = IngredientDisplayAdapter()

        with(binding) {
            btnOut.setOnClickListener {
                findNavController().popBackStack()
            }

            btnEdit.setOnClickListener {
                findNavController().navigate(RecipeInfoFragmentDirections.actionNavRecipeInfoToNavAdding(recipe))
            }

            rwIngredients.layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.VERTICAL, false
            )

            rwIngredients.adapter = adapter

            twTitle.text = recipe.title
            twTime.text = getStringFromRes(R.string.time_display) + recipe.time

            if (recipe.recipeInfo.isEmpty()) twRecipeInfo.visibility = View.GONE
            else twRecipeInfo.text = recipe.recipeInfo

            twCategory.text = getStringFromRes(R.string.category_display) + recipe.category

            val ingredients = recipe.ingredients.toMutableMap()
            if (ingredients.containsKey("")) {
                ingredients.remove("")
            }

            adapter.submitList(ingredients.toList())
        }

        return binding.root
    }

    private fun dataTransferError() {
        toast(getStringFromRes(R.string.data_transfer_error))
        findNavController().popBackStack()
    }

    override fun onDestroy() {
        activity?.findViewById<Toolbar>(R.id.toolbar)?.visibility = View.VISIBLE
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavView)?.visibility = View.VISIBLE
        super.onDestroy()
    }
}