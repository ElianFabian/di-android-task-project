package com.elian.myapplication.ui.tasklist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elian.myapplication.R
import com.elian.myapplication.data.model.Task
import com.elian.myapplication.data.repository.TaskStaticRepository
import com.elian.myapplication.databinding.FragmentTaskListBinding
import com.elian.myapplication.ui.tasklist.adapter.TaskAdapter

class TaskListFragment : Fragment(),
    ITaskListContract.IView,
    ITaskListContract.IOnRepositoryCallback
{
    private lateinit var binding: FragmentTaskListBinding

    private lateinit var taskAdapter: TaskAdapter

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

        initUI()
    }

    //endregion

    //region Methods

    private fun initUI()
    {
        initRecyclerViewAdapter()

        binding.fab.setOnClickListener()
        {
            NavHostFragment.findNavController(this).navigate(R.id.action_taskListFragment_to_taskAddFragment)
        }
    }

    private fun initRecyclerViewAdapter()
    {
        taskAdapter = TaskAdapter(
            arrayListOf()
        ) {
            Toast.makeText(
                context,
                "You pressed a task.",
                Toast.LENGTH_SHORT
            ).show()
        }

        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        binding.rvTasks.layoutManager = layoutManager
        binding.rvTasks.adapter = taskAdapter
    }
    //endregion


    //region ITaskListContract.IView

    override fun showProgress()
    {
    }

    override fun hideProgress()
    {
    }

    //endregion

    //region IRepositoryListCallback

    override fun onSuccess(list: List<Task>)
    {
        taskAdapter.load(list)
    }

    override fun onNoData()
    {
        Toast.makeText(context, "There's no data", Toast.LENGTH_SHORT).show()
    }

    //endregion
}