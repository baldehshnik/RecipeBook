package com.firstapplication.recipebook.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.firstapplication.recipebook.R
import com.firstapplication.recipebook.databinding.FragmentHubBinding

class HubFragment : Fragment(R.layout.fragment_hub) {

    private lateinit var binding: FragmentHubBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHubBinding.bind(view)
    }

}