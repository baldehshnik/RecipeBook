package com.firstapplication.recipebook.ui.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.firstapplication.recipebook.App
import com.firstapplication.recipebook.R
import com.firstapplication.recipebook.databinding.FragmentRecipeSearchingBinding
import com.firstapplication.recipebook.extensions.appComponent
import com.firstapplication.recipebook.sealed.RecipeListItemClick
import com.firstapplication.recipebook.ui.adapters.RecipeAdapter
import com.firstapplication.recipebook.ui.interfacies.OnRecipeItemClickListener
import com.firstapplication.recipebook.ui.models.RecipeModel
import com.firstapplication.recipebook.ui.viewmodels.RecipeSearchingViewModel
import com.firstapplication.recipebook.ui.viewmodels.factories.OnlyRecipeRepositoryViewModelFactory
import javax.inject.Inject

class RecipeSearchingFragment : Fragment(R.layout.fragment_recipe_searching),
    OnRecipeItemClickListener {

    private lateinit var binding: FragmentRecipeSearchingBinding

    @Inject
    lateinit var onlyRecipeRepositoryViewModelFactory: OnlyRecipeRepositoryViewModelFactory.Factory

    private val viewModel: RecipeSearchingViewModel by viewModels {
        onlyRecipeRepositoryViewModelFactory.create(activity?.application as App)
    }

    private lateinit var adapter: RecipeAdapter

    override fun onAttach(context: Context) {
        context.applicationContext.appComponent.inject(this)
        activity?.findViewById<Toolbar>(R.id.toolbar)?.title = ""
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRecipeSearchingBinding.bind(view)

        adapter = RecipeAdapter(this)

        with(binding) {
            btnOut.setOnClickListener {
                findNavController().popBackStack()
            }

            rwRecipes.layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.VERTICAL, false
            )

            rwRecipes.adapter = adapter
        }

        viewModel.searchRecipesList.observe(viewLifecycleOwner) { recipesList ->
            adapter.submitList(recipesList)
        }

        addTextChangedListener()

    }

    private fun addTextChangedListener() =
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(inputText: Editable?) {
                when (inputText?.length) {
                    0 -> viewModel.removeObserve()
                    else -> viewModel.setObserve(inputText.toString())
                }
                binding.rwRecipes.recycledViewPool.clear()
            }
        })

    override fun onItemClick(view: View, recipeModel: RecipeModel, recipeKey: RecipeListItemClick) {
        when (recipeKey) {
            is RecipeListItemClick.OnMarkerClick -> updateRecipe(recipeModel)
            is RecipeListItemClick.OnFullItemClick -> findNavController().navigate(
                RecipeSearchingFragmentDirections.actionNavSearchToNavRecipeInfo(recipeModel)
            )
            is RecipeListItemClick.OnItemClickInDeleteMode ->
                Toast.makeText(requireContext(), "Error!", Toast.LENGTH_LONG).show()
        }
    }

    private fun updateRecipe(recipeModel: RecipeModel) {
        recipeModel.isSaved = !recipeModel.isSaved
        viewModel.updateRecipeInDB(recipeModel)
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.findViewById<Toolbar>(R.id.toolbar)?.title =
            resources.getString(R.string.app_name)
    }

}