package com.firstapplication.recipebook.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.firstapplication.recipebook.App
import com.firstapplication.recipebook.R
import com.firstapplication.recipebook.databinding.FragmentRecipeAddingBinding
import com.firstapplication.recipebook.extensions.appComponent
import com.firstapplication.recipebook.ui.adapters.DishCategoryAdapter
import com.firstapplication.recipebook.ui.adapters.IngredientAdapter
import com.firstapplication.recipebook.ui.interfacies.OnCategoryItemClickListener
import com.firstapplication.recipebook.ui.viewmodels.RecipeAddingViewModel
import com.firstapplication.recipebook.ui.viewmodels.factories.OnlyRecipeRepositoryViewModelFactory
import javax.inject.Inject
import kotlin.random.Random


class RecipeAddingFragment : Fragment(R.layout.fragment_recipe_adding),
    OnCategoryItemClickListener {

    private lateinit var binding: FragmentRecipeAddingBinding

    @Inject
    lateinit var onlyRecipeRepositoryViewModelFactory: OnlyRecipeRepositoryViewModelFactory.Factory

    private val viewModel: RecipeAddingViewModel by viewModels {
        onlyRecipeRepositoryViewModelFactory.create(activity?.application as App)
    }

    private lateinit var inputMethodManager: InputMethodManager

    override fun onAttach(context: Context) {
        context.applicationContext.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener("ingredientKey") { _, bundle ->
            viewModel.addNewIngredient(
                bundle.getString("ingredientName").toString(),
                bundle.getString("ingredientCountFullName").toString()
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRecipeAddingBinding.bind(view)

        inputMethodManager = requireContext()
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        val dishCategoryAdapter = DishCategoryAdapter(getCategoriesList().toList(), this)
        val ingredientAdapter = IngredientAdapter()

        with(binding) {
            rwCategory.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )

            rwIngredients.adapter = ingredientAdapter

            rwCategory.adapter = dishCategoryAdapter

            btnAddIngredient.setOnClickListener { openAddingIngredientsFragment() }

            etTitle.setOnFocusChangeListener { _, hasFocus ->
                onFocusChangeListener(hasFocus = hasFocus, etTitle)
            }

            etTime.setOnFocusChangeListener { _, hasFocus ->
                onFocusChangeListener(hasFocus = hasFocus, etTitle)
            }

            etTimeType.setOnFocusChangeListener { _, hasFocus ->
                onFocusChangeListener(hasFocus = hasFocus, etTitle)
            }

            btnSave.setOnClickListener {
                when {
                    etTitle.length() == 0 -> etTitle.requestFocus()
                    etTime.length() == 0 -> etTime.requestFocus()
                    etTimeType.length() == 0 -> etTimeType.requestFocus()
                }
            }
        }

        viewModel.ingredients.observe(viewLifecycleOwner) { ingredientsMap ->
            ingredientAdapter.submitList(ingredientsMap.toList())
        }

    }

    override fun onPause() {
        super.onPause()
        inputMethodManager.hideSoftInputFromWindow(
            activity?.window?.decorView?.windowToken, 0
        )
    }

    private fun onFocusChangeListener(hasFocus: Boolean, editText: EditText) {
        if (hasFocus) {
            inputMethodManager.showSoftInput(
                editText,
                InputMethodManager.SHOW_FORCED
            )
        } else {
            inputMethodManager.hideSoftInputFromWindow(
                editText.windowToken,
                0
            )
        }
    }

    private fun getCategoriesList() = resources.getStringArray(
        R.array.dish_categories
    )

    private fun openAddingIngredientsFragment() =
        findNavController().navigate(R.id.navAddingIngredient)

    override fun onCategoryItemClick(categoryName: String) {

    }

}