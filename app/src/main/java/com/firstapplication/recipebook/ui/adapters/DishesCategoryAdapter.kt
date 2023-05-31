package com.firstapplication.recipebook.ui.adapters

import android.annotation.SuppressLint
import android.graphics.*
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.firstapplication.recipebook.R
import com.firstapplication.recipebook.databinding.CategoryItemBinding
import com.firstapplication.recipebook.ui.interfacies.OnCategoryItemClickListener
import com.firstapplication.recipebook.ui.models.DishCategoryModel

class DishesCategoryAdapter(
    private val categoryList: List<DishCategoryModel>,
    private val listener: OnCategoryItemClickListener
) : RecyclerView.Adapter<DishesCategoryAdapter.DishesCategoryViewHolder>() {

    class DishesCategoryViewHolder(private val binding: CategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(entity: DishCategoryModel) = with(binding) {
            twCategoryName.text = entity.name
            Glide.with(imgIcon.context)
                .load(entity.imgUrl)
                .placeholder(R.drawable.ic_baseline_image_search_24)
                .apply(RequestOptions().circleCrop())
                .error(R.drawable.ic_baseline_hide_image_24)
                .into(binding.imgIcon)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishesCategoryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return DishesCategoryViewHolder(CategoryItemBinding.inflate(layoutInflater))
    }

    override fun onBindViewHolder(
        holder: DishesCategoryViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        holder.bind(categoryList[position])
        holder.itemView.findViewById<ImageView>(R.id.imgIcon).setOnClickListener { view ->
            if (position == RecyclerView.NO_POSITION) return@setOnClickListener
            view.startAnimation(AnimationUtils.loadAnimation(view.context, R.anim.small_scale))
            listener.onCategoryItemClick(categoryList[position].name)
        }
    }

    override fun getItemCount(): Int = categoryList.size
}