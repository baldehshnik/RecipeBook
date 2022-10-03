package com.firstapplication.recipebook.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.firstapplication.recipebook.databinding.IngredientItemBinding
import android.text.method.ScrollingMovementMethod
import com.firstapplication.recipebook.ui.interfacies.OnIngredientDeleteItemClickListener

class IngredientAdapter(private val listenerDelete: OnIngredientDeleteItemClickListener) :
    ListAdapter<Pair<String, String>, IngredientAdapter.IngredientViewHolder>(IngredientDiffUtil()) {

    class IngredientViewHolder(private val binding: IngredientItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(pair: Pair<String, String>, listenerDelete: OnIngredientDeleteItemClickListener) = with(binding) {
            twIngredientName.apply {
                twIngredientName.text = pair.first
                twIngredientName.setHorizontallyScrolling(true)
                twIngredientName.movementMethod = ScrollingMovementMethod()
            }

            twIngredientCount.apply {
                twIngredientCount.text = pair.second
                twIngredientCount.setHorizontallyScrolling(true)
                twIngredientCount.movementMethod = ScrollingMovementMethod()
            }

            btnDeleteIngredient.setOnClickListener { view ->
                listenerDelete.onIngredientDeleteItemClick(view = view, ingredient = pair)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return IngredientViewHolder(IngredientItemBinding.inflate(layoutInflater))
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        holder.bind(getItem(position), listenerDelete)
    }

}