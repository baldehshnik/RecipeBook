package com.firstapplication.recipebook.ui.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.firstapplication.recipebook.R
import com.firstapplication.recipebook.databinding.RecipeItemBinding
import com.firstapplication.recipebook.enums.Colors
import com.firstapplication.recipebook.sealed.RecipeListItemClick
import com.firstapplication.recipebook.ui.adapters.RecipeAdapter.RecipeViewHolder
import com.firstapplication.recipebook.ui.fragments.DeleteMode
import com.firstapplication.recipebook.ui.interfacies.OnRecipeItemClickListener
import com.firstapplication.recipebook.ui.models.RecipeModel
import com.google.android.material.card.MaterialCardView

class RecipeAdapter(
    private val listener: OnRecipeItemClickListener
) : ListAdapter<RecipeModel, RecipeViewHolder>(RecipeDiffUtil()) {

    class RecipeViewHolder(
        private val binding: RecipeItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(recipeModel: RecipeModel) = with(binding) {
            twTitle.text = recipeModel.title
            twCategory.text = recipeModel.category
            twHours.text = recipeModel.time
            twDescription.text = recipeModel.recipeInfo

            if (!DeleteMode.isDeleteMode) {
                btnRadioDelete.visibility = View.GONE
                cardView.strokeColor = Color.parseColor(Colors.WHITE.color)
            }
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

//            notifyItemChanged(position)

            listener.notifyItemThatMarkerClicked(position = position)
        }

        setImageResource(item.isSaved)

        holder.itemView.findViewById<MaterialCardView>(R.id.cardView)
            .animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.list_anim)
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