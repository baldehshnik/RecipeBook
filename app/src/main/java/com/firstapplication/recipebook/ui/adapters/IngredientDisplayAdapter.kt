package com.firstapplication.recipebook.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.firstapplication.recipebook.databinding.IngredientDisplayBinding

class IngredientDisplayAdapter
    : ListAdapter<Pair<String, String>, IngredientDisplayAdapter.IngredientDisplayViewHolder>(
    IngredientDiffUtil()
) {

    class IngredientDisplayViewHolder(private val binding: IngredientDisplayBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(pair: Pair<String, String>) = with(binding) {
            twIngredientName.text = pair.first
            twIngredientCount.text = pair.second
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientDisplayViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return IngredientDisplayViewHolder(IngredientDisplayBinding.inflate(layoutInflater))
    }

    override fun onBindViewHolder(holder: IngredientDisplayViewHolder, position: Int) {
        holder.bind(pair = getItem(position))
    }
}