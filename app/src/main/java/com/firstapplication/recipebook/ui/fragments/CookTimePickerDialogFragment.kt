package com.firstapplication.recipebook.ui.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.firstapplication.recipebook.R
import com.firstapplication.recipebook.databinding.FragmentCookTimePickerDialogBinding
import com.firstapplication.recipebook.ui.fragments.RecipeAddingFragment.Companion.TIMEPICKER_CONNECT_KEY
import com.firstapplication.recipebook.ui.fragments.RecipeAddingFragment.Companion.TIMEPICKER_HOURS_KEY
import com.firstapplication.recipebook.ui.fragments.RecipeAddingFragment.Companion.TIMEPICKER_MINUTES_KEY

class CookTimePickerDialogFragment : DialogFragment(R.layout.fragment_cook_time_picker_dialog) {

    private lateinit var binding: FragmentCookTimePickerDialogBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = context?.let { AlertDialog.Builder(it) }
        binding = FragmentCookTimePickerDialogBinding.inflate(requireActivity().layoutInflater)

        builder?.setView(binding.root)
        builder?.setPositiveButton(R.string.confirm) { _, _ ->
            parentFragmentManager.setFragmentResult(
                TIMEPICKER_CONNECT_KEY, bundleOf(
                    TIMEPICKER_HOURS_KEY to binding.myTimePicker.selectedHour,
                    TIMEPICKER_MINUTES_KEY to binding.myTimePicker.selectedMinute,
                )
            )
            dismiss()
        }
        builder?.setNegativeButton(R.string.close, null)
        return builder?.create() ?: super.onCreateDialog(savedInstanceState)
    }
}