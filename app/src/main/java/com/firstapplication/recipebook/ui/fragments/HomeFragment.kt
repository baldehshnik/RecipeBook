package com.firstapplication.recipebook.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.marginBottom
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.firstapplication.recipebook.R
import com.firstapplication.recipebook.databinding.FragmentHomeBinding
import com.firstapplication.recipebook.extensions.appComponent
import com.firstapplication.recipebook.sealed.RecipeListItemClick
import com.firstapplication.recipebook.ui.adapters.DishesCategoryAdapter
import com.firstapplication.recipebook.ui.adapters.RecipeAdapter
import com.firstapplication.recipebook.ui.interfacies.OnCategoryItemClickListener
import com.firstapplication.recipebook.ui.interfacies.OnRecipeItemClickListener
import com.firstapplication.recipebook.ui.models.RecipeModel
import com.firstapplication.recipebook.ui.viewmodels.HomeViewModel
import com.firstapplication.recipebook.ui.viewmodels.factories.HomeViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView
import javax.inject.Inject
import androidx.navigation.NavOptions

class HomeFragment : BasicFragment(), OnRecipeItemClickListener, OnCategoryItemClickListener {
    private var actionBarSize = 0
    private var selectedCategory = "Все"

    private lateinit var binding: FragmentHomeBinding
    private lateinit var recipeAdapter: RecipeAdapter

    @Inject
    lateinit var factory: HomeViewModelFactory

    private val viewModel: HomeViewModel by viewModels { factory }

    override fun onAttach(context: Context) {
        context.applicationContext.appComponent.inject(this)
        super.onAttach(context)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        recipeAdapter = RecipeAdapter(this)
        with(binding) {
            actionBarSize = rwRecipes.marginBottom

            btnSearch.setOnClickListener { navigateToSearchFragment() }

            rwRecipes.layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.VERTICAL, false
            )
            rwRecipes.adapter = recipeAdapter

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
            binding.twDelete.text = "$count ${getStringFromRes(R.string.count_item_selected)}"
            if (count == 0) {
                disableDeleteWindow()
                setToolBarText()
                binding.rwCategories.visibility = View.VISIBLE
            }
        }

        binding.rwCategories.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.HORIZONTAL, false
        )

        viewModel.readCategories()
        viewModel.categories.observe(viewLifecycleOwner) {
            binding.rwCategories.adapter = DishesCategoryAdapter(it, this)
        }

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        if (DeleteMode.isDeleteMode) {
            disableDeleteWindow()
            viewModel.clearSelectedRecipe()
        }
    }

    private fun navigateToSearchFragment() {
        findNavController().navigate(
            R.id.navSearch, null,
            NavOptions.Builder()
                .setEnterAnim(R.anim.to_left_in).setExitAnim(R.anim.to_left_out)
                .setPopEnterAnim(R.anim.to_right_in).setPopExitAnim(R.anim.to_right_out)
                .build()
        )
    }

    private fun getBottomNavView() = activity?.findViewById<BottomNavigationView>(R.id.bottomNavView)

    private fun disableDeletedWindowView() = with(binding) {
        btnSearch.visibility = View.VISIBLE
        btnDeleteSelectedRecipe.visibility = View.GONE
        btnCloseDeletedWindow.visibility = View.GONE
        twDelete.visibility = View.GONE

        getBottomNavView()?.visibility = View.VISIBLE
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun disableDeleteWindow() {
        DeleteMode.isDeleteMode = false
        disableDeletedWindowView()
        recipeAdapter.notifyDataSetChanged()
        setNewMarginsToRecyclerView(marginBottom = actionBarSize, marginTop = 180f)
    }

    private fun clearToolBarText() {
        activity?.findViewById<Toolbar>(R.id.toolbar)?.title = ""
    }

    private fun setToolBarText() {
        activity?.findViewById<Toolbar>(R.id.toolbar)?.title = getStringFromRes(R.string.app_name)
    }

    override fun onItemClick(view: View, recipeModel: RecipeModel, recipeKey: RecipeListItemClick) =
        when (if (DeleteMode.isDeleteMode) RecipeListItemClick.OnItemClickInDeleteMode else recipeKey) {
            is RecipeListItemClick.OnMarkerClick -> updateRecipe(recipeModel = recipeModel)
            is RecipeListItemClick.OnFullItemClick -> findNavController().navigate(
                HomeFragmentDirections.actionNavHomeToNavRecipeInfo(recipeModel)
            )
            is RecipeListItemClick.OnItemClickInDeleteMode -> {
                val radioButton = view.findViewById<RadioButton>(R.id.btnRadioDelete)
                setRadioButtonVisibility(
                    view = view,
                    radioButton = radioButton,
                    recipeModel = recipeModel
                )
            }
        }

    private fun setStrokeColor(view: View) {
        view.findViewById<MaterialCardView>(R.id.cardView).strokeColor =
            ContextCompat.getColor(requireContext(), R.color.bottom_nav_view)
    }

    private fun deleteStrokeColor(view: View) {
        view.findViewById<MaterialCardView>(R.id.cardView).strokeColor =
            ContextCompat.getColor(requireContext(), R.color.white)
    }

    private fun updateRecipe(recipeModel: RecipeModel) {
        recipeModel.isSaved = !recipeModel.isSaved
        viewModel.updateRecipeInDB(recipeModel)
    }

    private fun setRadioButtonVisibility(
        view: View,
        radioButton: RadioButton,
        recipeModel: RecipeModel
    ) {
        when (radioButton.visibility) {
            View.VISIBLE -> {
                viewModel.deleteSelectedRecipe(recipeModel = recipeModel)
                radioButton.visibility = View.GONE
                deleteStrokeColor(view = view)
            }
            else -> {
                viewModel.addNewSelectedRecipes(recipeModel = recipeModel)
                radioButton.isChecked = true
                radioButton.visibility = View.VISIBLE
                setStrokeColor(view = view)
            }
        }
    }

    override fun onItemLongClick(view: View, recipeModel: RecipeModel): Boolean {
        if (!DeleteMode.isDeleteMode) {
            val btnRadioButton = view.findViewById<RadioButton>(R.id.btnRadioDelete)

            view.findViewById<MaterialCardView>(R.id.cardView).strokeColor =
                ContextCompat.getColor(requireContext(), R.color.bottom_nav_view)

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
            setNewMarginsToRecyclerView()
            clearToolBarText()

            viewModel.addNewSelectedRecipes(recipeModel = recipeModel)

            DeleteMode.isDeleteMode = true
        }

        return super.onItemLongClick(view, recipeModel)
    }

    private fun setNewMarginsToRecyclerView(marginBottom: Int = 0, marginTop: Float = 56f) {
        val params = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT
        )

        params.topToBottom = binding.rwCategories.id
        params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
        params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID

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

            if (categoryName == resources.getString(R.string.all)) {
                viewModel.changeRecipeList("")
            } else {
                viewModel.changeRecipeList(categoryName)
            }
        }
    }

    override fun notifyItemThatMarkerClicked(position: Int) {
        recipeAdapter.notifyItemChanged(position)
    }
}