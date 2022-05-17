package com.elian.taskproject.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

open class RecyclerViewAdapter<T>(
    @LayoutRes private val itemLayout: Int,
    private val list: MutableList<T> = mutableListOf(),
) :
    RecyclerView.Adapter<RecyclerViewAdapter<T>.ViewHolder>()
{
    private var onItemClickListener = OnItemClickListener<T> { _, _, _ -> }
    private var onItemLongClickListener = OnItemLongClickListener<T> { _, _, _ -> false }
    private var onBindViewHolderListener = OnBindViewHolderListener<T> { _, _, _ -> }

    fun interface OnItemClickListener<T>
    {
        fun onItemClick(v: View?, selectedItem: T, position: Int)
    }

    fun interface OnItemLongClickListener<T>
    {
        fun onItemLongClick(v: View?, selectedItem: T, position: Int): Boolean
    }

    fun interface OnBindViewHolderListener<T>
    {
        fun onBindViewHolder(view: View, item: T, position: Int)
    }

    val isEmpty get() = itemCount == 0

    fun clearList()
    {
        list.clear()
        notifyDataSetChanged()
    }

    fun replaceList(newList: List<T>)
    {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    fun addItem(item: T)
    {
        list.add(item)
        notifyItemInserted(itemCount)
    }

    fun insertItem(position: Int, item: T)
    {
        list.add(position, item)
        notifyItemInserted(itemCount)
    }

    fun insertItems(insertPosition: Int, items: List<T>)
    {
        list.addAll(insertPosition, items)
        notifyItemRangeInserted(insertPosition, items.size)
    }

    fun updateItem(position: Int, updatedItem: T)
    {
        list[position] = updatedItem
        notifyItemChanged(position)
    }

    fun moveItem(fromPosition: Int, toPosition: Int, item: T)
    {
        list.removeAt(fromPosition)
        list.add(toPosition, item)
        notifyItemMoved(fromPosition, toPosition)
    }

    /**
     * @return False if the list doesn't contains the item.
     */
    fun removeItem(item: T): Boolean
    {
        if (list.contains(item))
        {
            val position = list.indexOf(item)

            list.remove(item)
            notifyItemRemoved(position)

            return true
        }

        return false
    }

    fun removeItem(position: Int)
    {
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    fun removeItems(fromPosition: Int, count: Int)
    {
        val toPosition = fromPosition + count

        list.subList(fromPosition, toPosition).clear()
        notifyItemRangeRemoved(fromPosition, toPosition)
    }

    fun setOnItemClickListener(listener: OnItemClickListener<T>)
    {
        onItemClickListener = listener
    }

    fun setOnItemLongClickListener(listener: OnItemLongClickListener<T>)
    {
        onItemLongClickListener = listener
    }

    fun setOnBindViewHolderListener(listener: OnBindViewHolderListener<T>)
    {
        onBindViewHolderListener = listener
    }

    //region RecyclerView.Adapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val layoutInflater = LayoutInflater.from(parent.context)

        val view = layoutInflater.inflate(itemLayout, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val item = list[position]

        holder.itemView.setOnClickListener(holder)
        holder.itemView.setOnLongClickListener(holder)

        onBindViewHolderListener.onBindViewHolder(holder.itemView, item, position)
    }

    override fun getItemCount(): Int = list.size

    //endregion

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener,
        View.OnLongClickListener
    {
        override fun onClick(v: View?)
        {
            onItemClickListener.onItemClick(v, list[layoutPosition], layoutPosition)
        }

        override fun onLongClick(v: View?): Boolean
        {
            return onItemLongClickListener.onItemLongClick(v, list[layoutPosition], layoutPosition)
        }
    }
}
