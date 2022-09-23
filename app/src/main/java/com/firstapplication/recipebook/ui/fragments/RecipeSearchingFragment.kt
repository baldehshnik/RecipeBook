package com.firstapplication.recipebook.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.firstapplication.recipebook.R
import com.firstapplication.recipebook.databinding.FragmentRecipeSearchingBinding

class RecipeSearchingFragment : Fragment(R.layout.fragment_recipe_searching) {

    private lateinit var binding: FragmentRecipeSearchingBinding

    override fun onAttach(context: Context) {
        activity?.findViewById<Toolbar>(R.id.toolbar)?.title = ""
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRecipeSearchingBinding.bind(view)

        binding.btnOut.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.findViewById<Toolbar>(R.id.toolbar)?.title =
            resources.getString(R.string.app_name)
    }

}