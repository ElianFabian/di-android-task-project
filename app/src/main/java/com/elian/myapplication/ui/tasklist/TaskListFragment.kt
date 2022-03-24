package com.elian.myapplication.ui.tasklist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elian.myapplication.R
import com.elian.myapplication.databinding.FragmentTaskListBinding
import com.elian.myapplication.ui.tasklist.adapter.TaskAdapter

class TaskListFragment : Fragment()
{
    private lateinit var binding: FragmentTaskListBinding

    private lateinit var adapter: TaskAdapter

    //region Fragment Methods

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View
    {
        binding = FragmentTaskListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        binding.fab.setOnClickListener()
        {
            NavHostFragment.findNavController(this).navigate(R.id.action_taskListFragment_to_taskAddFragment)
        }
    }
    
    //endregion
    
    //region Methods
     
     private fun initAdapter()
     {
         adapter = TaskAdapter(listOf())
         val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
     
         binding.rvTasks.layoutManager = layoutManager
         binding.rvTasks.adapter = adapter
     }
    
    //endregion
}