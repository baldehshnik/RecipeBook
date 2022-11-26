package com.firstapplication.recipebook.ui.adapters

import androidx.recyclerview.widget.DiffUtil

class IngredientDiffUtil : DiffUtil.ItemCallback<Pair<String, String>>() {

    override fun areItemsTheSame(
        oldItem: Pair<String, String>,
        newItem: Pair<String, String>
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: Pair<String, String>,
        newItem: Pair<String, String>
    ): Boolean {
        return oldItem.first == newItem.first
    }
}