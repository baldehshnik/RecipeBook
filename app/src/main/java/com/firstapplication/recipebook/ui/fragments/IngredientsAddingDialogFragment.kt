package com.firstapplication.recipebook.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.firstapplication.recipebook.App
import com.firstapplication.recipebook.R
import com.firstapplication.recipebook.databinding.DialogFragmentIngredientsAddingBinding
import com.firstapplication.recipebook.extensions.appComponent
import com.firstapplication.recipebook.ui.viewmodels.RecipeAddingViewModel
import com.firstapplication.recipebook.ui.viewmodels.factories.OnlyRecipeRepositoryViewModelFactory
import javax.inject.Inject

class IngredientsAddingDialogFragment :
    DialogFragment(R.layout.dialog_fragment_ingredients_adding) {

    private lateinit var binding: DialogFragmentIngredientsAddingBinding

    @Inject
    lateinit var onlyRecipeRepositoryViewModelFactory: OnlyRecipeRepositoryViewModelFactory.Factory

    private val viewModel: RecipeAddingViewModel by viewModels {
        onlyRecipeRepositoryViewModelFactory.create(activity?.application as App)
    }

    override fun onAttach(context: Context) {
        context.applicationContext.appComponent.inject(this)
        super.onAttach(context)
    }

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = DialogFragmentIngredientsAddingBinding.bind(view)

        with(binding) {
            btnConfirmIngredientAdding.setOnClickListener {
                if (etIngredientAdding.length() != 0 && etIngredientCount.length() != 0) {

                    val ingredientName = etIngredientAdding.text.toString()

                    parentFragmentManager.setFragmentResult(
                        "ingredientKey", bundleOf(
                            "ingredientName" to ingredientName,
                            "ingredientCountFullName" to getIngredientCount()
                        )
                    )

                    dismiss()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Fill ingredient name",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            btnCancel.setOnClickListener {
                dismiss()
            }
        }

    }

    private fun getIngredientCount(): String = with(binding) {
        val ingredientCount = etIngredientCount.text.toString()
        val ingredientType = etIngredientTypeCount.text.toString()

        return@with if (ingredientType != "") "$ingredientCount $ingredientType" else ingredientCount
    }

}