package com.firstapplication.recipebook.ui.interfacies

import androidx.recyclerview.widget.RecyclerView

interface OnItemMoveListener {
    fun onMove(fromPosition: Int, toPosition: Int)
    fun onStartDrag(viewHolder: RecyclerView.ViewHolder)
}