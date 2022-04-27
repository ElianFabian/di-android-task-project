package com.elian.taskproject.ui.tasklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elian.taskproject.R
import com.elian.taskproject.base.BaseFragment
import com.elian.taskproject.data.model.Task
import com.elian.taskproject.databinding.FragmentTaskListBinding
import com.elian.taskproject.extensions.navigate
import com.elian.taskproject.ui.tasklist.adapter.TaskAdapter

class TaskListFragment : BaseFragment(),
    ITaskListContract.IView,
    TaskAdapter.IRecyclerViewItemClickListener
{
    private lateinit var binding: FragmentTaskListBinding
    private lateinit var taskAdapter: TaskAdapter
    override lateinit var presenter: ITaskListContract.IPresenter

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

    //endregion

    //region Methods

    private fun initUI()
    {
        initRecyclerViewAdapter()

        presenter.getList()

        binding.fab.setOnClickListener()
        {
            navigate(R.id.action_taskListFragment_to_taskManagerFragment)
        }
    }

    private fun initRecyclerViewAdapter()
    {
        taskAdapter = TaskAdapter(arrayListOf(), this)

        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        binding.rvTasks.layoutManager = layoutManager
        binding.rvTasks.adapter = taskAdapter
    }

    private fun sendSelectedTask_To_TaskEditFragment(task: Task)
    {
        navigate(R.id.action_taskListFragment_to_taskManagerFragment, Bundle().apply()
        {
            putSerializable("selectedTask", task)
        })
    }

    //endregion

    //region ITaskListContract.IView

    override fun showProgress()
    {
        binding.progressBar.isVisible = true
    }

    override fun hideProgress()
    {
        binding.progressBar.isVisible = false
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

    //region TaskAdapter.IRecyclerViewItemClickListener

    override fun onItemClick(v: View?, selectedTask: Task)
    {
        sendSelectedTask_To_TaskEditFragment(selectedTask)
    }

    override fun onItemLongClick(v: View?, selectedTask: Task): Boolean
    {
        // TODO: to implement delete option

        return true
    }

    //endregion
}