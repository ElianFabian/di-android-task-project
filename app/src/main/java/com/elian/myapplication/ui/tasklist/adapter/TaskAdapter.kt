package com.elian.myapplication.ui.tasklist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elian.myapplication.R
import com.elian.myapplication.data.model.Task
import com.elian.myapplication.databinding.ItemTaskBinding

class TaskAdapter(
    private val tasks: List<Task>,
) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>()
{
    //region RecyclerView.Adapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder
    {
        val layoutInflater = LayoutInflater.from(parent.context)

        return TaskViewHolder(layoutInflater.inflate(R.layout.item_task, parent, false))
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int)
    {
        val taskItem = tasks[position]

        holder.render(taskItem)
    }

    override fun getItemCount(): Int = tasks.size

    //endregion

    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        private val binding = ItemTaskBinding.bind(view)

        fun render(task: Task) = with(binding)
        {

        }
    }
}