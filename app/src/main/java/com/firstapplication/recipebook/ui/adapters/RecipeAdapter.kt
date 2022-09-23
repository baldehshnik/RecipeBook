package com.firstapplication.recipebook.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.firstapplication.recipebook.R
import com.firstapplication.recipebook.databinding.RecipeItemBinding
import com.firstapplication.recipebook.ui.adapters.RecipeAdapter.RecipeViewHolder
import com.firstapplication.recipebook.ui.interfacies.OnRecipeItemClickListener
import com.firstapplication.recipebook.ui.models.RecipeModel

class RecipeAdapter(private val listener: OnRecipeItemClickListener) :
    ListAdapter<RecipeModel, RecipeViewHolder>(RecipeDiffUtil()) {

    class RecipeViewHolder(private val binding: RecipeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(recipeModel: RecipeModel, listener: OnRecipeItemClickListener) = with(binding) {
            twTitle.text = recipeModel.title
            twCount.text = recipeModel.ingredientsCount.toString()
            twHours.text = recipeModel.cookingTime.toString()
            twDescription.text = recipeModel.recipeInfo

            if (recipeModel.isSaved) btnMarker.setImageResource(R.drawable.ic_baseline_bookmark_24)
            else btnMarker.setImageResource(R.drawable.ic_baseline_bookmark_border_36)

            itemView.setOnClickListener { view ->
                listener.onItemClick(view = view, recipeModel = recipeModel)
            }

            btnMarker.setOnClickListener { view ->
                listener.onItemClick(view = view, recipeModel = recipeModel)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return RecipeViewHolder(RecipeItemBinding.inflate(layoutInflater))
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }

}

class RecipeDiffUtil : DiffUtil.ItemCallback<RecipeModel>() {
    override fun areItemsTheSame(oldItem: RecipeModel, newItem: RecipeModel): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: RecipeModel, newItem: RecipeModel): Boolean {
        return oldItem.id == newItem.id
    }
}