package com.firstapplication.recipebook.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.firstapplication.recipebook.R
import com.firstapplication.recipebook.databinding.RecipeItemBinding
import com.firstapplication.recipebook.sealed.RecipeListItemClick
import com.firstapplication.recipebook.ui.adapters.RecipeAdapter.RecipeViewHolder
import com.firstapplication.recipebook.ui.fragments.DeleteMode
import com.firstapplication.recipebook.ui.interfacies.OnRecipeItemClickListener
import com.firstapplication.recipebook.ui.models.RecipeModel

class RecipeAdapter(
    private val listener: OnRecipeItemClickListener
) :
    ListAdapter<RecipeModel, RecipeViewHolder>(RecipeDiffUtil()) {

    class RecipeViewHolder(
        private val binding: RecipeItemBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(recipeModel: RecipeModel) = with(binding) {

            twTitle.text = getTitleText(recipeModel.title)
            twCategory.text = recipeModel.category
            twHours.text = recipeModel.cookingTime.toString() + " " + recipeModel.timeType
            twDescription.text = getRecipeInfoText(recipeModel.recipeInfo)

            if (!DeleteMode.isDeleteMode)
                btnRadioDelete.visibility = View.GONE
        }

        private fun getRecipeInfoText(text: String): String {
            return if (text.length > 52) text.substring(0, 52) + "..." else text
        }

        private fun getTitleText(text: String): String {
            return if (text.length > 11) text.substring(0, 11) + "..." else text
        }

        fun setImageResource(key: Boolean) = with(binding) {
            if (key) btnMarker.setImageResource(R.drawable.ic_baseline_bookmark_36)
            else btnMarker.setImageResource(R.drawable.ic_baseline_bookmark_border_36)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return RecipeViewHolder(RecipeItemBinding.inflate(layoutInflater))
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) = with(holder) {
        val item = getItem(position)

        bind(item)

        itemView.setOnClickListener { view ->
            listener.onItemClick(
                view = view,
                recipeModel = item,
                RecipeListItemClick.OnFullItemClick
            )
        }

        itemView.setOnLongClickListener { view ->
            listener.onItemLongClick(view = view, recipeModel = item)
        }

        itemView.findViewById<ImageButton>(R.id.btnMarker).setOnClickListener {
            listener.onItemClick(
                view = itemView,
                recipeModel = item,
                RecipeListItemClick.OnMarkerClick
            )

            listener.notifyItemThatMarkerClicked(position = position)
        }

        setImageResource(item.isSaved)
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