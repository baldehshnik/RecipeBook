package com.firstapplication.recipebook.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginBottom
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.firstapplication.recipebook.App
import com.firstapplication.recipebook.R
import com.firstapplication.recipebook.databinding.FragmentHomeBinding
import com.firstapplication.recipebook.extensions.appComponent
import com.firstapplication.recipebook.sealed.RecipeListItemClick
import com.firstapplication.recipebook.ui.adapters.DishCategoryAdapter
import com.firstapplication.recipebook.ui.adapters.RecipeAdapter
import com.firstapplication.recipebook.ui.interfacies.OnCategoryItemClickListener
import com.firstapplication.recipebook.ui.interfacies.OnRecipeItemClickListener
import com.firstapplication.recipebook.ui.models.RecipeModel
import com.firstapplication.recipebook.ui.viewmodels.HomeViewModel
import com.firstapplication.recipebook.ui.viewmodels.factories.OnlyRecipeRepositoryViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import javax.inject.Inject

class HomeFragment : Fragment(R.layout.fragment_home), OnRecipeItemClickListener,
    OnCategoryItemClickListener {

    private var actionBarSize = 0
    private var selectedCategory = "Все"

    private lateinit var binding: FragmentHomeBinding

    @Inject
    lateinit var onlyRecipeRepositoryViewModelFactory: OnlyRecipeRepositoryViewModelFactory.Factory

    private val viewModel: HomeViewModel by viewModels {
        onlyRecipeRepositoryViewModelFactory.create(activity?.application as App)
    }

    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var categoryAdapter: DishCategoryAdapter

    override fun onAttach(context: Context) {
        context.applicationContext.appComponent.inject(this)
        super.onAttach(context)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        recipeAdapter = RecipeAdapter(this@HomeFragment)
        categoryAdapter = DishCategoryAdapter(getCategoryList(), this)

        with(binding) {
            actionBarSize = rwRecipes.marginBottom

            btnSearch.setOnClickListener {
                findNavController().navigate(R.id.navSearch)
            }

            rwRecipes.layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.VERTICAL, false
            )

            rwCategories.layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL, false
            )

            rwRecipes.adapter = recipeAdapter
            rwCategories.adapter = categoryAdapter

            btnCloseDeletedWindow.setOnClickListener {
                disableDeleteWindow()
                setToolBarText()
                viewModel.clearSelectedRecipe()
                rwCategories.visibility = View.VISIBLE
            }

            btnDeleteSelectedRecipe.setOnClickListener {
                viewModel.deleteSelectedRecipesFromDB()
                DeleteMode.isDeleteMode = false
                disableDeleteWindow()
                setToolBarText()
                rwCategories.visibility = View.VISIBLE
            }
        }

        viewModel.recipesList.observe(viewLifecycleOwner) { recipeModels ->
            recipeAdapter.submitList(recipeModels)
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

    private fun getCategoryList() = resources.getStringArray(R.array.dish_categories).toList()

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
        setNewMarginsToRecyclerView(marginBottom = actionBarSize, marginTop = 100f)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun notifyAdapterDataSetChanged() = recipeAdapter.notifyDataSetChanged()

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
            is RecipeListItemClick.OnFullItemClick -> findNavController().navigate(
                HomeFragmentDirections.actionNavHomeToNavRecipeInfo(recipeModel)
            )
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
                rwCategories.visibility = View.GONE
            }

            getBottomNavView()?.visibility = View.GONE

            clearToolBarText()

            DeleteMode.isDeleteMode = true

            viewModel.addNewSelectedRecipes(recipeModel = recipeModel)

            setNewMarginsToRecyclerView()
        }

        return super.onItemLongClick(view, recipeModel)
    }

    private fun setNewMarginsToRecyclerView(marginBottom: Int = 0, marginTop: Float = 50f) {
        val params = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT
        )

        params.bottomMargin = marginBottom

        params.topMargin = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, marginTop, resources.displayMetrics
        ).toInt()

        binding.rwRecipes.layoutParams = params
    }

    override fun onCategoryItemClick(categoryName: String) {
        if (selectedCategory != categoryName) {
            binding.rwRecipes.recycledViewPool.clear()

            selectedCategory = categoryName

            if (categoryName == getCategoryList()[0]) viewModel.changeRecipeList(category = "")
            else viewModel.changeRecipeList(category = categoryName)
        }
    }

}