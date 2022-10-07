package com.firstapplication.recipebook.ui.fragments

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.firstapplication.recipebook.R
import com.firstapplication.recipebook.databinding.FragmentCookTimePickerDialogBinding
import com.firstapplication.recipebook.enums.IngredientsKeys
import com.firstapplication.recipebook.enums.TimePickerKeys

class CookTimePickerDialogFragment : DialogFragment(R.layout.fragment_cook_time_picker_dialog) {

    private lateinit var binding: FragmentCookTimePickerDialogBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCookTimePickerDialogBinding.bind(view)

        with(binding) {
            tpTime.setIs24HourView(true)
            btnConfirm.setOnClickListener {
                parentFragmentManager.setFragmentResult(
                    TimePickerKeys.CONNECT_KEY.id, bundleOf(
                        TimePickerKeys.HOURS_KEY.id to binding.tpTime.hour.toString(),
                        TimePickerKeys.MINUTES_KEY.id to binding.tpTime.minute.toString(),
                    )
                )
                dismiss()
            }
        }

    }

}