package com.elian.taskproject.util

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

fun createSwipeComponent(swipeDirs: Int, onSwiped: (RecyclerView.ViewHolder, Int) -> Unit): ItemTouchHelper
{
    return ItemTouchHelper(
        object : ItemTouchHelper.SimpleCallback(0, swipeDirs)
        {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean
            {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int)
            {
                onSwiped(viewHolder, direction)
            }
        }
    )
}