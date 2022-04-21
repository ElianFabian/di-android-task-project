package com.elian.taskproject.ui.tasklist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elian.taskproject.R
import com.elian.taskproject.data.model.Task
import com.elian.taskproject.databinding.FragmentTaskListBinding
import com.elian.taskproject.ui.tasklist.adapter.TaskAdapter

class TaskListFragment : Fragment(),
    ITaskListContract.IView
{
    private lateinit var binding: FragmentTaskListBinding
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var presenter: ITaskListContract.IPresenter

    //region Fragment Methods

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        presenter = TaskListPresenter(this)
    }

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

    override fun onDestroy()
    {
        super.onDestroy()

        presenter.onDestroy()
    }

    //endregion

    //region Methods

    private fun initUI()
    {
        initRecyclerViewAdapter()

        presenter.load()

        binding.fab.setOnClickListener()
        {
            NavHostFragment.findNavController(this).navigate(R.id.action_taskListFragment_to_taskAddFragment)
        }
    }

    private fun initRecyclerViewAdapter()
    {
        taskAdapter = TaskAdapter(arrayListOf())
        {
            Toast.makeText(context, "You pressed a task.", Toast.LENGTH_SHORT).show()
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