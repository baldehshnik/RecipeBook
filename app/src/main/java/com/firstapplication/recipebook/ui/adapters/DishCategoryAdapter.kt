package com.firstapplication.recipebook.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.firstapplication.recipebook.R
import com.firstapplication.recipebook.databinding.DishCategoryItemBinding
import com.firstapplication.recipebook.ui.interfacies.OnCategoryItemClickListener

class DishCategoryAdapter(
    private val categoryList: List<String>,
    private val listener: OnCategoryItemClickListener
) :
    RecyclerView.Adapter<DishCategoryAdapter.DishCategoryViewHolder>() {

    companion object {
        var lastPressedItem = 0
    }

    class DishCategoryViewHolder(private val binding: DishCategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: String) = with(binding) {
            twCategoryItem.text = category
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishCategoryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return DishCategoryViewHolder(DishCategoryItemBinding.inflate(layoutInflater))
    }

    override fun onBindViewHolder(holder: DishCategoryViewHolder, position: Int) {
        holder.bind(categoryList[position])

        with(holder.itemView) {

            val view = findViewById<TextView>(R.id.twCategoryItem)

            setNewTextViewStyle(view = view, position = position)

            setOnClickListener {
                if (position == RecyclerView.NO_POSITION) return@setOnClickListener

                notifyItemChanged(lastPressedItem)
                lastPressedItem = position
                notifyItemChanged(lastPressedItem)

                listener.onCategoryItemClick(categoryName = categoryList[position])
            }
        }
    }

    private fun setNewTextViewStyle(view: TextView, position: Int) =
        if (lastPressedItem == position)
            setNewBackgroundAndColor(
                view = view,
                background = R.drawable.category_selected,
                color = R.color.custom_text
            )
        else setNewBackgroundAndColor(
            view = view,
            background = R.drawable.category_default,
            color = R.color.dark_grey
        )

    private fun setNewBackgroundAndColor(
        view: TextView, background: Int, color: Int
    ) = with(view) {
        setBackgroundResource(background)
        setTextColor(ContextCompat.getColor(context, color))
    }

    override fun getItemCount(): Int = categoryList.size

}