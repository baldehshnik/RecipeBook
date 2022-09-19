package com.firstapplication.recipebook.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.firstapplication.recipebook.R
import com.firstapplication.recipebook.databinding.FragmentHomeBinding
import com.firstapplication.recipebook.extensions.appComponent

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding

    override fun onAttach(context: Context) {
        context.applicationContext.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
    }

}