package com.firstapplication.recipebook.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.firstapplication.recipebook.ui.viewmodels.factories.HubViewModelFactory
import javax.inject.Inject

class HubFragment : BaseFragment(), OnRecipeItemClickListener {
    private lateinit var binding: FragmentHubBinding
    private lateinit var recipeAdapter: RecipeAdapter

    @Inject
    lateinit var factory: HubViewModelFactory

    @Inject
    lateinit var inputMethodManager: InputMethodManager

    @Inject
    lateinit var onEditTextFocusChangeListener: OnEditTextFocusChangeListener

    override val viewModel: HubViewModel by viewModels { factory }

    override fun onAttach(context: Context) {
        context.applicationContext.appComponent.inject(this)
        super.onAttach(context)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHubBinding.inflate(layoutInflater, container, false)
        recipeAdapter = RecipeAdapter(this)

        binding.btnAdd.setOnClickListener { navigateToAddFragment() }

        with(binding) {
            btnSearch.setOnClickListener { setSearchMode() }

            rwRecipes.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )

            rwRecipes.adapter = recipeAdapter

            etSearchItem.setOnTouchListener { _, motionEvent ->
                if (motionEvent.action != MotionEvent.ACTION_UP) return@setOnTouchListener false

                etSearchItem.apply {
                    val intrinsicWidth = compoundDrawables[2].intrinsicWidth
                    if (motionEvent.x > width - paddingRight - intrinsicWidth) disableSearchMode()
                }

                return@setOnTouchListener false
            }

            etSearchItem.setOnFocusChangeListener { _, hasFocus ->
                onEditTextFocusChangeListener.setOnFocusChangeListener(
                    hasFocus = hasFocus, editText = etSearchItem
                )
            }

            etSearchItem.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(inputText: Editable?) {
//                    when (inputText?.length) {
//                        0 -> viewModel.setObserve("")
//                        else -> viewModel.setObserve(inputText.toString())
//                    }
                    binding.rwRecipes.recycledViewPool.clear()
                }

            })
        }

        viewModel.savedRecipeList.observe(viewLifecycleOwner) { recipeModel ->
            recipeAdapter.submitList(recipeModel)
        }

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        inputMethodManager.closeKeyboard(activity)
    }

    private fun navigateToAddFragment() {
        findNavController().navigate(
            HubFragmentDirections.actionNavHubToNavAdding(),
            NavOptions.Builder()
                .setEnterAnim(R.anim.to_top_in).setExitAnim(R.anim.to_top_out)
                .setPopEnterAnim(R.anim.to_bottom_in).setPopExitAnim(R.anim.to_bottom_out)
                .build()
            )
    }

    private fun setSearchMode() = with(binding) {
        etSearchItem.visibility = View.VISIBLE
        btnSearch.visibility = View.GONE
        etSearchItem.requestFocus()
        btnAdd.visibility = View.GONE
    }

    private fun disableSearchMode() = with(binding) {
        btnSearch.visibility = View.VISIBLE
        etSearchItem.visibility = View.GONE
        etSearchItem.text.clear()
        btnAdd.visibility = View.VISIBLE
    }

    override fun onItemClick(view: View, recipeModel: RecipeModel, recipeKey: RecipeListItemClick) {
        when (recipeKey) {
            is RecipeListItemClick.OnMarkerClick -> updateRecipeMarker(recipeModel)
            is RecipeListItemClick.OnFullItemClick -> onFullItemClick(recipeModel)
            is RecipeListItemClick.OnItemClickInDeleteMode -> toast(getStringFromRes(R.string.error))
        }
    }

    override fun notifyItemThatMarkerClicked(position: Int) {
        recipeAdapter.notifyItemChanged(position)
    }

    private fun onFullItemClick(recipeModel: RecipeModel) {
        findNavController().navigate(HubFragmentDirections.actionNavHubToNavRecipeInfo(recipeModel))
        binding.etSearchItem.setText("")
    }

    private fun updateRecipeMarker(recipeModel: RecipeModel) {
        recipeModel.isSaved = !recipeModel.isSaved
        viewModel.updateRecipeInDB(recipeModel)
    }
}