package com.firstapplication.recipebook.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.firstapplication.recipebook.databinding.IngredientItemBinding
import android.text.method.ScrollingMovementMethod

class IngredientAdapter :
    ListAdapter<Pair<String, String>, IngredientAdapter.IngredientViewHolder>(IngredientDiffUtil()) {

    class IngredientViewHolder(private val binding: IngredientItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(pair: Pair<String, String>) = with(binding) {
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
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return IngredientViewHolder(IngredientItemBinding.inflate(layoutInflater))
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

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
