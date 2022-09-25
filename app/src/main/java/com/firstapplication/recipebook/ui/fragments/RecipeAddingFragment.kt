package com.firstapplication.recipebook.ui.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.firstapplication.recipebook.App
import com.firstapplication.recipebook.R
import com.firstapplication.recipebook.databinding.FragmentRecipeAddingBinding
import com.firstapplication.recipebook.extensions.appComponent
import com.firstapplication.recipebook.ui.adapters.DishCategoryAdapter
import com.firstapplication.recipebook.ui.interfacies.OnCategoryItemClickListener
import com.firstapplication.recipebook.ui.viewmodels.RecipeAddingViewModel
import com.firstapplication.recipebook.ui.viewmodels.factories.OnlyRecipeRepositoryViewModelFactory
import javax.inject.Inject

class RecipeAddingFragment : Fragment(R.layout.fragment_recipe_adding),
    OnCategoryItemClickListener {

    private lateinit var binding: FragmentRecipeAddingBinding

    @Inject
    lateinit var onlyRecipeRepositoryViewModelFactory: OnlyRecipeRepositoryViewModelFactory.Factory

    private val viewModel: RecipeAddingViewModel by viewModels {
        onlyRecipeRepositoryViewModelFactory.create(activity?.application as App)
    }

    override fun onAttach(context: Context) {
        context.applicationContext.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRecipeAddingBinding.bind(view)

        val adapter = DishCategoryAdapter(getCategoriesList().toList(), this)

        with(binding) {
            rwCategory.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )

            rwCategory.adapter = adapter



        }
    }

    private fun getCategoriesList() = resources.getStringArray(
        R.array.dish_categories
    )

    override fun onCategoryItemClick(categoryName: String) {

    }

}