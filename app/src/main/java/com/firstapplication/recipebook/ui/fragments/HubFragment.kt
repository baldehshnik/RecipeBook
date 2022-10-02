package com.firstapplication.recipebook.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import androidx.fragment.app.Fragment
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.firstapplication.recipebook.App
import com.firstapplication.recipebook.R
import com.firstapplication.recipebook.databinding.FragmentHubBinding
import com.firstapplication.recipebook.extensions.appComponent
import com.firstapplication.recipebook.extensions.closeKeyboard
import com.firstapplication.recipebook.sealed.RecipeListItemClick
import com.firstapplication.recipebook.ui.adapters.RecipeAdapter
import com.firstapplication.recipebook.ui.interfacies.OnEditTextFocusChangeListener
import com.firstapplication.recipebook.ui.interfacies.OnRecipeItemClickListener
import com.firstapplication.recipebook.ui.models.RecipeModel
import com.firstapplication.recipebook.ui.viewmodels.HubViewModel
import com.firstapplication.recipebook.ui.viewmodels.factories.OnlyRecipeRepositoryViewModelFactory
import javax.inject.Inject

class HubFragment : Fragment(R.layout.fragment_hub), OnRecipeItemClickListener {

    private lateinit var binding: FragmentHubBinding

    @Inject
    lateinit var onlyRecipeRepositoryViewModelFactory: OnlyRecipeRepositoryViewModelFactory.Factory

    private val viewModel: HubViewModel by viewModels {
        onlyRecipeRepositoryViewModelFactory.create(activity?.application as App)
    }

    @Inject
    lateinit var inputMethodManager: InputMethodManager

    @Inject
    lateinit var onEditTextFocusChangeListener: OnEditTextFocusChangeListener

    override fun onAttach(context: Context) {
        context.applicationContext.appComponent.inject(this)
        super.onAttach(context)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHubBinding.bind(view)

        val adapter = RecipeAdapter(this)

        with(binding) {
            btnSearch.setOnClickListener {
                etSearchItem.visibility = View.VISIBLE
                btnSearch.visibility = View.GONE
                etSearchItem.requestFocus()
            }

            rwRecipes.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )

            rwRecipes.adapter = adapter

            etSearchItem.setOnTouchListener { _, motionEvent ->
                if (motionEvent.action != MotionEvent.ACTION_UP) return@setOnTouchListener false

                val drawable = etSearchItem.compoundDrawables[2]

                if (motionEvent.x > etSearchItem.width
                    - etSearchItem.paddingRight
                    - drawable.intrinsicWidth
                ) {
                    btnSearch.visibility = View.VISIBLE
                    etSearchItem.visibility = View.GONE
                    etSearchItem.text.clear()
                }

                return@setOnTouchListener false
            }

            etSearchItem.setOnFocusChangeListener { _, hasFocus ->
                onEditTextFocusChangeListener.setOnFocusChangeListener(
                    hasFocus = hasFocus, editText = etSearchItem
                )
            }

            etSearchItem.addTextChangedListener(object : TextWatcher {

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })
        }

        viewModel.savedRecipeList.observe(viewLifecycleOwner) { recipeModel ->
            adapter.submitList(recipeModel)
        }

    }

    override fun onPause() {
        super.onPause()
        inputMethodManager.closeKeyboard(activity)
    }

    override fun onItemClick(
        view: View,
        recipeModel: RecipeModel,
        recipeKey: RecipeListItemClick
    ) {
        when (recipeKey) {
            is RecipeListItemClick.OnFullItemClick -> findNavController().navigate(R.id.navRecipeInfo)
            else -> {
                recipeModel.isSaved = !recipeModel.isSaved
                viewModel.updateRecipeInDB(recipeModel = recipeModel)
            }
        }
    }
}