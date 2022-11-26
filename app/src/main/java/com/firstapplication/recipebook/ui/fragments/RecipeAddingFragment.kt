package com.firstapplication.recipebook.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firstapplication.recipebook.App
import com.firstapplication.recipebook.R
import com.firstapplication.recipebook.databinding.FragmentRecipeAddingBinding
import com.firstapplication.recipebook.extensions.appComponent
import com.firstapplication.recipebook.ui.adapters.DishCategoryAdapter
import com.firstapplication.recipebook.ui.adapters.IngredientAdapter
import com.firstapplication.recipebook.extensions.closeKeyboard
import com.firstapplication.recipebook.ui.adapters.IngredientCallback
import com.firstapplication.recipebook.ui.interfacies.OnCategoryItemClickListener
import com.firstapplication.recipebook.ui.interfacies.OnIngredientDeleteItemClickListener
import com.firstapplication.recipebook.ui.interfacies.OnItemMoveListener
import com.firstapplication.recipebook.ui.listeners.OnEditTextFocusChangeListenerImpl
import com.firstapplication.recipebook.ui.models.RecipeModel
import com.firstapplication.recipebook.ui.viewmodels.RecipeAddingViewModel
import com.firstapplication.recipebook.ui.viewmodels.factories.OnlyRecipeRepositoryViewModelFactory
import javax.inject.Inject

class RecipeAddingFragment : BasicFragment(), OnCategoryItemClickListener,
    OnIngredientDeleteItemClickListener, OnItemMoveListener {

    private var isSaved = false
    private var time = ""

    private val recipeArgs: RecipeAddingFragmentArgs by navArgs()

    private lateinit var binding: FragmentRecipeAddingBinding

    private lateinit var itemTouchHelper: ItemTouchHelper

    @Inject
    lateinit var onlyRecipeRepositoryViewModelFactory: OnlyRecipeRepositoryViewModelFactory.Factory

    @Inject
    lateinit var inputMethodManager: InputMethodManager

    @Inject
    lateinit var onEditTextFocusChangeListener: OnEditTextFocusChangeListenerImpl

    private val viewModel: RecipeAddingViewModel by viewModels {
        onlyRecipeRepositoryViewModelFactory.create(activity?.application as App)
    }

    override fun onAttach(context: Context) {
        context.applicationContext.appComponent.inject(this)
        activity?.findViewById<Toolbar>(R.id.toolbar)?.title = ""
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(INGREDIENT_CONNECT_KEY) { _, bundle ->
            viewModel.addNewIngredient(
                bundle.getString(INGREDIENT_NAME_KEY).toString(),
                bundle.getString(INGREDIENT_COUNT_FULL_NAME_KEY).toString()
            )
        }

        setFragmentResultListener(TIMEPICKER_CONNECT_KEY) { _, bundle ->
            bundle.apply {
                time = viewModel.getCookingTime(
                    getString(TIMEPICKER_HOURS_KEY).toString(),
                    getString(TIMEPICKER_MINUTES_KEY).toString()
                )
                binding.twTime.text = time
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecipeAddingBinding.inflate(layoutInflater, container, false)

        val categoriesList = getCategoriesArray().toList()

        val dishCategoryAdapter = DishCategoryAdapter(categoriesList.subList(1, categoriesList.size), this)
        val ingredientAdapter = IngredientAdapter(this, this)

        val callback = IngredientCallback(ingredientAdapter)
        itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.rwIngredients)

        if (recipeArgs.currentRecipe != null) {
            val recipe = recipeArgs.currentRecipe!!
            setCurrentRecipeData(recipe = recipe)
            dishCategoryAdapter.setNewSelectedItem(
                getCurrentRecipeCategoryID(
                    recipe = recipe,
                    categoriesList = categoriesList
                )
            )

            isSaved = true
        }

        with(binding) {
            rwCategory.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )

            rwIngredients.adapter = ingredientAdapter
            rwCategory.adapter = dishCategoryAdapter

            btnEditTime.setOnClickListener { navigateTo(R.id.navTimePick) }

            btnAddIngredient.setOnClickListener { navigateTo(R.id.navAddingIngredient) }

            btnSave.setOnClickListener { onSaveClick() }

            setOnFocusChangeListener()
        }

        viewModel.ingredients.observe(viewLifecycleOwner) { ingredientsMap ->
            if (ingredientsMap.containsKey("")) ingredientsMap.remove("")
            ingredientAdapter.submitList(ingredientsMap.toList())
        }

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        inputMethodManager.closeKeyboard(activity = activity)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (recipeArgs.currentRecipe == null) {
            activity?.findViewById<Toolbar>(R.id.toolbar)?.title = getStringFromRes(R.string.app_name)
        }
    }

    override fun onCategoryItemClick(categoryName: String) {
        viewModel.setNewRecipeCategory(categoryName = categoryName)
    }

    override fun onIngredientDeleteItemClick(view: View, ingredient: Pair<String, String>) {
        if (view is ImageButton) viewModel.deleteIngredient(ingredient.first)
    }

    override fun onMove(fromPosition: Int, toPosition: Int) {
        viewModel.changeIngredientsPosition(fromPosition, toPosition)
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }

    private fun onSaveClick() = with(binding) {
        when {
            etTitle.length() == 0 -> etTitle.requestFocus()
            time.isEmpty() -> navigateTo(R.id.navTimePick)
            else -> {
                createNewRecipe()
                findNavController().popBackStack()
            }
        }
    }

    private fun getCurrentRecipeCategoryID(recipe: RecipeModel, categoriesList: List<String>): Int {
        for ((i, item) in categoriesList.subList(1, categoriesList.size).withIndex())
            if (item == recipe.category)
                return i
        return 0
    }

    @SuppressLint("SetTextI18n")
    private fun setCurrentRecipeData(recipe: RecipeModel) = with(binding) {
        etTitle.setText(recipe.title, TextView.BufferType.EDITABLE)

        twTime.text = recipe.time
        time = recipe.time

        etCookingInfo.setText(recipe.recipeInfo, TextView.BufferType.EDITABLE)

        viewModel.setCurrentRecipeIngredients(recipe.ingredients)

        btnBack.visibility = View.VISIBLE
        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setOnFocusChangeListener() = with(binding) {
        etTitle.setOnFocusChangeListener { _, hasFocus ->
            onEditTextFocusChangeListener.setOnFocusChangeListener(
                hasFocus = hasFocus, editText = etTitle
            )
        }
    }

    private fun navigateTo(@IdRes id: Int) = findNavController().navigate(id)

    private fun getCategoriesArray() = getStringArrayFromRes(R.array.dish_categories)

    private fun createNewRecipe() {
        val title = binding.etTitle.text.toString().trim()
        val recipeInfo = binding.etCookingInfo.text.toString()

        when(isSaved) {
            false -> viewModel.createRecipe(
                title = title,
                recipeInfo = recipeInfo,
                time = time
            )
            true -> viewModel.updateRecipeInDB(
                recipeModel = recipeArgs.currentRecipe ?: return,
                title = title,
                recipeInfo = recipeInfo,
                time = time
            )
        }
    }

    companion object {
        const val INGREDIENT_CONNECT_KEY = "connection_ingredient_key"
        const val INGREDIENT_NAME_KEY = "name_ingredient_key"
        const val INGREDIENT_COUNT_FULL_NAME_KEY = "count_full_name_key"

        const val TIMEPICKER_CONNECT_KEY = "timepicker_connection_key"
        const val TIMEPICKER_HOURS_KEY = "timepicker_hours_key"
        const val TIMEPICKER_MINUTES_KEY = "timepicker_minutes_key"
    }
}