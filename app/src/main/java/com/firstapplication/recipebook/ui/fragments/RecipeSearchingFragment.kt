package com.firstapplication.recipebook.ui.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isInvisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.firstapplication.recipebook.R
import com.firstapplication.recipebook.databinding.FragmentRecipeSearchingBinding
import com.firstapplication.recipebook.extensions.appComponent
import com.firstapplication.recipebook.sealed.RecipeListItemClick
import com.firstapplication.recipebook.ui.adapters.RecipeAdapter
import com.firstapplication.recipebook.ui.interfacies.OnRecipeItemClickListener
import com.firstapplication.recipebook.ui.models.RecipeModel
import com.firstapplication.recipebook.ui.viewmodels.RecipeSearchingViewModel
import com.firstapplication.recipebook.ui.viewmodels.factories.OnlyRecipeRepositoryViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import javax.inject.Inject

class RecipeSearchingFragment : BasicFragment(), OnRecipeItemClickListener {
    private lateinit var binding: FragmentRecipeSearchingBinding
    private lateinit var recipeAdapter: RecipeAdapter

    @Inject
    lateinit var factory: OnlyRecipeRepositoryViewModelFactory

    private val viewModel: RecipeSearchingViewModel by viewModels {
        factory
    }

    override fun onAttach(context: Context) {
        context.applicationContext.appComponent.inject(this)
        activity?.findViewById<Toolbar>(R.id.toolbar)?.title = ""
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavView)?.visibility = View.GONE

        setNewBottomMarginToBottomNavView()
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecipeSearchingBinding.inflate(layoutInflater, container, false)
        recipeAdapter = RecipeAdapter(this)

        with(binding) {
            btnOut.setOnClickListener {
                btnOut.isInvisible = true
                findNavController().popBackStack()
            }

            rwRecipes.layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.VERTICAL, false
            )

            rwRecipes.adapter = recipeAdapter
        }

        viewModel.searchRecipesList.observe(viewLifecycleOwner) { recipesList ->
            recipeAdapter.submitList(recipesList)
        }

        addTextChangedListener()
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavView)?.visibility = View.VISIBLE
        activity?.findViewById<Toolbar>(R.id.toolbar)?.title = getStringFromRes(R.string.app_name)
    }

    override fun onItemClick(view: View, recipeModel: RecipeModel, recipeKey: RecipeListItemClick) {
        when (recipeKey) {
            is RecipeListItemClick.OnMarkerClick -> updateRecipe(recipeModel)
            is RecipeListItemClick.OnFullItemClick -> findNavController().navigate(
                RecipeSearchingFragmentDirections.actionNavSearchToNavRecipeInfo(recipeModel)
            )
            is RecipeListItemClick.OnItemClickInDeleteMode -> toast(getStringFromRes(R.string.error))
        }
    }

    override fun notifyItemThatMarkerClicked(position: Int) {
        recipeAdapter.notifyItemChanged(position)
    }

    private fun setNewBottomMarginToBottomNavView(bottomMargin: Int = 0) {
        val layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT
        )

        layoutParams.setMargins(0, 0, 0, bottomMargin)
        activity?.findViewById<View>(R.id.homeHostFragment)?.layoutParams = layoutParams
    }

    private fun addTextChangedListener() {
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
    }

    private fun updateRecipe(recipeModel: RecipeModel) {
        recipeModel.isSaved = !recipeModel.isSaved
        viewModel.updateRecipeInDB(recipeModel)
    }
}