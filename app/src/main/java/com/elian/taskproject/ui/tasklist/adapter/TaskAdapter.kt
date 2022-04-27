package com.elian.taskproject.ui.tasklist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elian.taskproject.R
import com.elian.taskproject.data.model.Task
import com.elian.taskproject.databinding.ItemTaskBinding
import java.util.*
import kotlin.collections.ArrayList

class TaskAdapter(
    private val tasks: ArrayList<Task>,
    private val itemListener: IRecyclerViewClickListener
) :
    RecyclerView.Adapter<TaskAdapter.ViewHolder>()
{
    interface IOnClickAndLongClickListener : View.OnClickListener, View.OnLongClickListener

    // https://stackoverflow.com/questions/28296708/get-clicked-item-and-its-position-in-recyclerview
    interface IRecyclerViewClickListener
    {
        fun recyclerViewListClicked(v: View?, task: Task)
    }

    fun load(list: List<Task>)
    {
        tasks.clear()
        tasks.addAll(list)

        notifyDataSetChanged()
    }

    //region RecyclerView.Adapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val layoutInflater = LayoutInflater.from(parent.context)

        val view = layoutInflater.inflate(R.layout.item_task, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val taskItem = tasks[position]

        holder.render(taskItem)
    }

    override fun getItemCount(): Int = tasks.size

    //endregion

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener
    {
        private val binding = ItemTaskBinding.bind(view)

        private val importanceStringArray = binding.root.resources.getStringArray(R.array.frgTaskAdd_spImportance_entries)

        fun render(task: Task) = with(binding)
        {
            root.setOnClickListener(this@ViewHolder)
            
            tvName.text = task.name
            tvImportance.text = importanceStringArray[task.importance.ordinal]

            task.endDateEstimated?.let()
            {
                swIsEndDate.isChecked = it < Calendar.getInstance().timeInMillis
            }
        }

        override fun onClick(v: View?)
        {
            itemListener.recyclerViewListClicked(v, tasks[layoutPosition])
        }
    }
}