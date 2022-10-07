package com.firstapplication.recipebook.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.firstapplication.recipebook.App
import com.firstapplication.recipebook.R
import com.firstapplication.recipebook.databinding.FragmentRecipeAddingBinding
import com.firstapplication.recipebook.extensions.appComponent
import com.firstapplication.recipebook.sealed.Error
import com.firstapplication.recipebook.ui.adapters.DishCategoryAdapter
import com.firstapplication.recipebook.ui.adapters.IngredientAdapter
import com.firstapplication.recipebook.enums.IngredientsKeys
import com.firstapplication.recipebook.extensions.closeKeyboard
import com.firstapplication.recipebook.ui.interfacies.OnCategoryItemClickListener
import com.firstapplication.recipebook.ui.interfacies.OnIngredientDeleteItemClickListener
import com.firstapplication.recipebook.ui.listeners.OnEditTextFocusChangeListenerImpl
import com.firstapplication.recipebook.ui.models.RecipeModel
import com.firstapplication.recipebook.ui.viewmodels.RecipeAddingViewModel
import com.firstapplication.recipebook.ui.viewmodels.factories.OnlyRecipeRepositoryViewModelFactory
import javax.inject.Inject

class RecipeAddingFragment : Fragment(R.layout.fragment_recipe_adding),
    OnCategoryItemClickListener, OnIngredientDeleteItemClickListener {

    private var isSaved = false

    private val recipeArgs: RecipeAddingFragmentArgs by navArgs()

    private lateinit var binding: FragmentRecipeAddingBinding

    @Inject
    lateinit var onlyRecipeRepositoryViewModelFactory: OnlyRecipeRepositoryViewModelFactory.Factory

    private val viewModel: RecipeAddingViewModel by viewModels {
        onlyRecipeRepositoryViewModelFactory.create(activity?.application as App)
    }

    @Inject
    lateinit var inputMethodManager: InputMethodManager

    @Inject
    lateinit var onEditTextFocusChangeListener: OnEditTextFocusChangeListenerImpl

    override fun onAttach(context: Context) {
        context.applicationContext.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(IngredientsKeys.CONNECT_KEY.key) { _, bundle ->
            viewModel.addNewIngredient(
                bundle.getString(IngredientsKeys.NAME_KEY.key).toString(),
                bundle.getString(IngredientsKeys.COUNT_FULL_NAME_KEY.key).toString()
            )
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRecipeAddingBinding.bind(view)

        val categoriesList = getCategoriesArray().toList()

        val dishCategoryAdapter =
            DishCategoryAdapter(categoriesList.subList(1, categoriesList.size), this)

        val ingredientAdapter = IngredientAdapter(this)

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

            btnAddIngredient.setOnClickListener { openAddingIngredientsFragment() }
            btnSave.setOnClickListener { btnSaveOnClick() }

            setOnFocusChangeListener()
        }

        viewModel.ingredients.observe(viewLifecycleOwner) { ingredientsMap ->
            if (ingredientsMap.containsKey("")) ingredientsMap.remove("")
            ingredientAdapter.submitList(ingredientsMap.toList())
        }

    }

    private fun btnSaveOnClick() = with(binding) {
        when {
            etTitle.length() == 0 -> etTitle.requestFocus()
            etTime.length() == 0 -> etTime.requestFocus()
            etTimeType.length() == 0 -> etTimeType.requestFocus()
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

    private fun setCurrentRecipeData(recipe: RecipeModel) = with(binding) {
        etTitle.setText(recipe.title, TextView.BufferType.EDITABLE)
        etTime.setText(recipe.cookingTime.toString(), TextView.BufferType.EDITABLE)
        etTimeType.setText(recipe.timeType, TextView.BufferType.EDITABLE)
        etCookingInfo.setText(recipe.recipeInfo, TextView.BufferType.EDITABLE)

        viewModel.setCurrentRecipeIngredients(recipe.ingredients)

        btnBack.visibility = View.VISIBLE

        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onPause() {
        super.onPause()
        inputMethodManager.closeKeyboard(activity = activity)
    }

    private fun setOnFocusChangeListener() = with(binding) {
        etTitle.setOnFocusChangeListener { _, hasFocus ->
            onEditTextFocusChangeListener.setOnFocusChangeListener(
                hasFocus = hasFocus, editText = etTitle
            )
        }

        etTime.setOnFocusChangeListener { _, hasFocus ->
            onEditTextFocusChangeListener.setOnFocusChangeListener(
                hasFocus = hasFocus, editText = etTime
            )
        }

        etTimeType.setOnFocusChangeListener { _, hasFocus ->
            onEditTextFocusChangeListener.setOnFocusChangeListener(
                hasFocus = hasFocus, editText = etTimeType
            )
        }
    }

    private fun getCategoriesArray() = resources.getStringArray(
        R.array.dish_categories
    )

    private fun openAddingIngredientsFragment() =
        findNavController().navigate(R.id.navAddingIngredient)

    override fun onCategoryItemClick(categoryName: String) {
        viewModel.setNewRecipeCategory(categoryName = categoryName)
    }

    override fun onIngredientDeleteItemClick(view: View, ingredient: Pair<String, String>) {
        if (view is ImageButton)
            viewModel.deleteIngredient(ingredient.first)
    }

    private fun createNewRecipe() {
        val title = binding.etTitle.text.toString()
        val recipeInfo = binding.etCookingInfo.text.toString()
        val timeType = binding.etTimeType.text.toString()
        val time = binding.etTime.text.toString()

        val checkTimeRes = viewModel.checkTime(time)
        when {
            checkTimeRes is Error.CorrectResult && !isSaved -> viewModel.createRecipe(
                title = title,
                recipeInfo = recipeInfo,
                timeType = timeType,
                time = time.toDouble()
            )
            checkTimeRes is Error.CorrectResult && isSaved -> viewModel.updateRecipeInDB(
                recipeArgs.currentRecipe!!, title = title, recipeInfo = recipeInfo,
                timeType = timeType, time = time.toDouble()
            )
            checkTimeRes is Error.ErrorResult -> Toast.makeText(
                requireContext(), "Time isn't correctly", Toast.LENGTH_LONG
            ).show()
        }
    }

}