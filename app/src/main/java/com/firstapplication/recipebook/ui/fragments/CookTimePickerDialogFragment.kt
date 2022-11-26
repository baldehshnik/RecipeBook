package com.firstapplication.recipebook.ui.fragments

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.firstapplication.recipebook.R
import com.firstapplication.recipebook.databinding.FragmentCookTimePickerDialogBinding
import com.firstapplication.recipebook.ui.fragments.RecipeAddingFragment.Companion.TIMEPICKER_CONNECT_KEY
import com.firstapplication.recipebook.ui.fragments.RecipeAddingFragment.Companion.TIMEPICKER_HOURS_KEY
import com.firstapplication.recipebook.ui.fragments.RecipeAddingFragment.Companion.TIMEPICKER_MINUTES_KEY


class CookTimePickerDialogFragment : DialogFragment(R.layout.fragment_cook_time_picker_dialog) {

    private lateinit var binding: FragmentCookTimePickerDialogBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCookTimePickerDialogBinding.bind(view)

        with(binding) {
            tpTime.setIs24HourView(true)

            tpTime.hour = 0
            tpTime.minute = 0

            btnConfirm.setOnClickListener {
                parentFragmentManager.setFragmentResult(
                    TIMEPICKER_CONNECT_KEY, bundleOf(
                        TIMEPICKER_HOURS_KEY to tpTime.hour.toString(),
                        TIMEPICKER_MINUTES_KEY to tpTime.minute.toString(),
                    )
                )

                dismiss()
            }

            btnClose.setOnClickListener { dismiss() }
        }
    }
}