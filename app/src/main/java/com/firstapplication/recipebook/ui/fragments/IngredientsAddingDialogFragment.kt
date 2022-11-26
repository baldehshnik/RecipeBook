package com.firstapplication.recipebook.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.firstapplication.recipebook.R
import com.firstapplication.recipebook.databinding.DialogFragmentIngredientsAddingBinding
import com.firstapplication.recipebook.ui.fragments.RecipeAddingFragment.Companion.INGREDIENT_CONNECT_KEY
import com.firstapplication.recipebook.ui.fragments.RecipeAddingFragment.Companion.INGREDIENT_COUNT_FULL_NAME_KEY
import com.firstapplication.recipebook.ui.fragments.RecipeAddingFragment.Companion.INGREDIENT_NAME_KEY

class IngredientsAddingDialogFragment : DialogFragment(R.layout.dialog_fragment_ingredients_adding) {

    private lateinit var binding: DialogFragmentIngredientsAddingBinding

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DialogFragmentIngredientsAddingBinding.bind(view)
        binding.btnConfirmIngredientAdding.setOnClickListener {
            if (binding.etIngredientAdding.length() != 0 && binding.etIngredientCount.length() != 0) {
                setFragmentResult(binding.etIngredientAdding.text.toString().trim())
                dismiss()
            } else {
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.fill_ingredient_name),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun setFragmentResult(name: String) {
        parentFragmentManager.setFragmentResult(
            INGREDIENT_CONNECT_KEY, bundleOf(
                INGREDIENT_NAME_KEY to name,
                INGREDIENT_COUNT_FULL_NAME_KEY to getIngredientCount()
            )
        )
    }

    private fun getIngredientCount(): String = with(binding) {
        val ingredientCount = etIngredientCount.text.toString().trim()
        val ingredientType = etIngredientTypeCount.text.toString().trim()

        return@with if (ingredientType != "") "$ingredientCount $ingredientType"
        else ingredientCount
    }
}