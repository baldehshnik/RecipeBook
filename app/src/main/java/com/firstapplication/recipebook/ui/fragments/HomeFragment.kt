package com.firstapplication.recipebook.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.firstapplication.recipebook.App
import com.firstapplication.recipebook.R
import com.firstapplication.recipebook.databinding.FragmentHomeBinding
import com.firstapplication.recipebook.extensions.appComponent
import com.firstapplication.recipebook.sealed.RecipeListItemClick
import com.firstapplication.recipebook.ui.adapters.RecipeAdapter
import com.firstapplication.recipebook.ui.interfacies.OnRecipeItemClickListener
import com.firstapplication.recipebook.ui.models.RecipeModel
import com.firstapplication.recipebook.ui.viewmodels.HomeViewModel
import com.firstapplication.recipebook.ui.viewmodels.factories.OnlyRecipeRepositoryViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import javax.inject.Inject

class HomeFragment : Fragment(R.layout.fragment_home), OnRecipeItemClickListener {

    private lateinit var binding: FragmentHomeBinding

    @Inject
    lateinit var onlyRecipeRepositoryViewModelFactory: OnlyRecipeRepositoryViewModelFactory.Factory

    private val viewModel: HomeViewModel by viewModels {
        onlyRecipeRepositoryViewModelFactory.create(activity?.application as App)
    }

    private lateinit var adapter: RecipeAdapter

    override fun onAttach(context: Context) {
        context.applicationContext.appComponent.inject(this)
        super.onAttach(context)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        adapter = RecipeAdapter(this@HomeFragment)

        with(binding) {
            btnSearch.setOnClickListener {
                findNavController().navigate(R.id.navSearch)
            }

            rwRecipes.layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.VERTICAL, false
            )

            rwRecipes.adapter = adapter

            btnCloseDeletedWindow.setOnClickListener {
                disableDeleteWindow()
                setToolBarText()
                viewModel.clearSelectedRecipe()
            }

            btnDeleteSelectedRecipe.setOnClickListener {
                viewModel.deleteSelectedRecipesFromDB()
                DeleteMode.isDeleteMode = false
                disableDeleteWindow()
                setToolBarText()
            }
        }

        viewModel.recipesList.observe(viewLifecycleOwner) { recipeModels ->
            adapter.submitList(recipeModels)
        }

        viewModel.selectedRecipesCount.observe(viewLifecycleOwner) { count ->
            binding.twDelete.text =
                "$count ${resources.getString(R.string.count_item_selected)}"
        }

    }

    override fun onPause() {
        super.onPause()

        if (DeleteMode.isDeleteMode) {
            disableDeleteWindow()
            viewModel.clearSelectedRecipe()
        }
    }

    private fun getBottomNavView(): BottomNavigationView? =
        activity?.findViewById(R.id.bottomNavView)

    private fun takeAwayDeletedWindow() = with(binding) {
        btnSearch.visibility = View.VISIBLE
        btnDeleteSelectedRecipe.visibility = View.GONE
        btnCloseDeletedWindow.visibility = View.GONE
        twDelete.visibility = View.GONE

        getBottomNavView()?.visibility = View.VISIBLE
    }

    private fun disableDeleteWindow() {
        DeleteMode.isDeleteMode = false
        takeAwayDeletedWindow()
        notifyAdapterDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun notifyAdapterDataSetChanged() = adapter.notifyDataSetChanged()

    private fun clearToolBarText() {
        activity?.findViewById<Toolbar>(R.id.toolbar)?.title = ""
    }

    private fun setToolBarText() {
        activity?.findViewById<Toolbar>(R.id.toolbar)?.title =
            resources.getString(R.string.app_name)
    }

    override fun onItemClick(view: View, recipeModel: RecipeModel, recipeKey: RecipeListItemClick) =
        when (if (DeleteMode.isDeleteMode) RecipeListItemClick.OnItemClickInDeleteMode else recipeKey) {
            is RecipeListItemClick.OnMarkerClick -> updateRecipe(recipeModel = recipeModel)
            is RecipeListItemClick.OnFullItemClick -> findNavController().navigate(R.id.navRecipeInfo)
            is RecipeListItemClick.OnItemClickInDeleteMode -> setRadioButtonVisibility(
                view.findViewById(
                    R.id.btnRadioDelete
                ), recipeModel = recipeModel
            )
        }

    private fun updateRecipe(recipeModel: RecipeModel) {
        recipeModel.isSaved = !recipeModel.isSaved
        viewModel.updateRecipeInDB(recipeModel = recipeModel)
    }

    private fun setRadioButtonVisibility(radioButton: RadioButton, recipeModel: RecipeModel) =
        when (radioButton.visibility) {
            View.VISIBLE -> {
                viewModel.deleteSelectedRecipe(recipeModel = recipeModel)
                radioButton.visibility = View.GONE
            }
            else -> {
                viewModel.addNewSelectedRecipes(recipeModel = recipeModel)
                radioButton.isChecked = true
                radioButton.visibility = View.VISIBLE
            }
        }

    override fun onItemLongClick(view: View, recipeModel: RecipeModel): Boolean {
        if (!DeleteMode.isDeleteMode) {
            val btnRadioButton = view.findViewById<RadioButton>(R.id.btnRadioDelete)

            btnRadioButton.visibility = View.VISIBLE
            btnRadioButton.isChecked = true

            with(binding) {
                btnSearch.visibility = View.GONE
                btnDeleteSelectedRecipe.visibility = View.VISIBLE
                btnCloseDeletedWindow.visibility = View.VISIBLE
                twDelete.visibility = View.VISIBLE
            }

            getBottomNavView()?.visibility = View.GONE

            clearToolBarText()

            DeleteMode.isDeleteMode = true

            viewModel.addNewSelectedRecipes(recipeModel = recipeModel)
        }

        return true
    }
}