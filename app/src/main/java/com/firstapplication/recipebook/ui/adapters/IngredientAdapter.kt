package com.firstapplication.recipebook.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.firstapplication.recipebook.databinding.IngredientItemBinding
import android.text.method.ScrollingMovementMethod
import android.widget.TextView
import com.firstapplication.recipebook.ui.interfacies.OnIngredientDeleteItemClickListener

class IngredientAdapter(private val listenerDelete: OnIngredientDeleteItemClickListener) :
    ListAdapter<Pair<String, String>, IngredientAdapter.IngredientViewHolder>(IngredientDiffUtil()) {

    class IngredientViewHolder(
        private val binding: IngredientItemBinding,
        private val listenerDelete: OnIngredientDeleteItemClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(pair: Pair<String, String>) = with(binding) {
            twIngredientName.apply {
                twIngredientName.text = pair.first
                setHorizontalScrolling(this)
            }

            twIngredientCount.apply {
                twIngredientCount.text = pair.second
                setHorizontalScrolling(this)
            }

            btnDeleteIngredient.setOnClickListener { view ->
                listenerDelete.onIngredientDeleteItemClick(view = view, ingredient = pair)
            }
        }

        private fun setHorizontalScrolling(textView: TextView) {
            textView.setHorizontallyScrolling(true)
            textView.movementMethod = ScrollingMovementMethod()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return IngredientViewHolder(
            IngredientItemBinding.inflate(layoutInflater),
            listenerDelete = listenerDelete
        )
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        holder.bind(pair = getItem(position))
    }

}