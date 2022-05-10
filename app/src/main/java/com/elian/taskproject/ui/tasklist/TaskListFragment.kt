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
import com.elian.taskproject.data.utils.RecyclerViewAdapter
import com.elian.taskproject.databinding.FragmentTaskListBinding
import com.elian.taskproject.databinding.ItemTaskBinding
import com.elian.taskproject.extensions.navigate
import java.util.*

class TaskListFragment : BaseFragment(),
    ITaskListContract.IView,
    RecyclerViewAdapter.OnBindViewHolderListener<Task>,
    RecyclerViewAdapter.OnItemClickListener<Task>,
    RecyclerViewAdapter.OnItemLongClickListener<Task>
{
    private lateinit var binding: FragmentTaskListBinding
    private lateinit var taskAdapter: RecyclerViewAdapter<Task>
    override lateinit var presenter: ITaskListContract.IPresenter

    private lateinit var importanceStringArray: Array<String>

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
        importanceStringArray = resources.getStringArray(R.array.frgTaskAdd_spnImportance_entries)

        initRecyclerViewAdapter()

        presenter.getList()

        binding.fab.setOnClickListener()
        {
            navigate(R.id.action_taskListFragment_to_taskManagerFragment)
        }
    }

    private fun initRecyclerViewAdapter()
    {
        taskAdapter = RecyclerViewAdapter(itemLayout = R.layout.item_task)

        binding.rvTasks.adapter = taskAdapter
        binding.rvTasks.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        taskAdapter.setOnBindViewHolderListener(this)
        taskAdapter.setOnItemClickListener(this)
        taskAdapter.setOnItemLongClickListener(this)
    }

    private fun sendSelectedTask_To_TaskEditFragment(task: Task, position: Int)
    {
        navigate(R.id.action_taskListFragment_to_taskManagerFragment, Bundle().apply()
        {
            putSerializable(getString(R.string.bundleKey_selectedTask), task)
            putInt(getString(R.string.bundleKey_selectedTask_position), position)
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

    override fun showNoDataImage()
    {
        binding.ivNoData.isVisible = true
    }

    override fun hideNoDataImage()
    {
        binding.ivNoData.isVisible = false
    }

    override fun onListSuccess(list: List<Task>)
    {
        hideNoDataImage()

        taskAdapter.replaceList(list)
    }

    override fun onNoData()
    {
        showNoDataImage()
    }

    override fun onDeleteSuccess(deletedTask: Task, position: Int)
    {
        taskAdapter.removeItem(deletedTask)

        if (taskAdapter.itemCount == 0) showNoDataImage()

        Toast.makeText(context, "The task number $position was successfully deleted.", Toast.LENGTH_SHORT).show()
    }

    //endregion

    //region RecyclerViewAdapter.OnBindViewHolderListener<>

    override fun onBindViewHolder(view: View, item: Task, position: Int)
    {
        ItemTaskBinding.bind(view).apply()
        {
            tvName.text = item.name
            tvImportance.text = importanceStringArray[item.importance.ordinal]

            item.estimatedEndDate?.let()
            {
                swIsEndDate.isChecked = it < Calendar.getInstance().timeInMillis
            }
        }
    }

    //endregion

    //region RecyclerViewAdapter.OnItemClickListener<>

    override fun onItemClick(v: View?, selectedItem: Task, position: Int)
    {
        sendSelectedTask_To_TaskEditFragment(selectedItem, position)
    }

    //endregion

    //region RecyclerViewAdapter.OnItemLongClickListener<>

    override fun onItemLongClick(v: View?, selectedItem: Task, position: Int): Boolean
    {
        presenter.delete(selectedItem)

        return true
    }

    //endregion
}