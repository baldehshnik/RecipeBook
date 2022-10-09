package com.firstapplication.recipebook.ui.interfacies

interface ItemTouchMoveListener {
    fun onMove(fromPosition: Int, toPosition: Int): Boolean
}