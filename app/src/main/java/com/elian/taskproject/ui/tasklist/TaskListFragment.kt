package com.elian.taskproject.ui.tasklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elian.taskproject.R
import com.elian.taskproject.base.BaseFragment
import com.elian.taskproject.data.model.Task
import com.elian.taskproject.util.RecyclerViewAdapter
import com.elian.taskproject.databinding.FragmentTaskListBinding
import com.elian.taskproject.databinding.ItemTaskBinding
import com.elian.taskproject.util.extensions.navigate
import com.elian.taskproject.util.extensions.toast
import kotlinx.coroutines.*
import java.util.*
import kotlin.concurrent.schedule

class TaskListFragment : BaseFragment(),
    TaskListContract.View,
    RecyclerViewAdapter.OnBindViewHolderListener<Task>,
    RecyclerViewAdapter.OnItemClickListener<Task>,
    RecyclerViewAdapter.OnItemLongClickListener<Task>
{
    override lateinit var presenter: TaskListContract.Presenter

    private lateinit var binding: FragmentTaskListBinding

    private val taskAdapter = RecyclerViewAdapter<Task>(itemLayout = R.layout.item_task)

    private lateinit var importanceStringArray: Array<String>

    private val deletedTasks = linkedMapOf<Task, Int>()
    private val completedTasks = mutableListOf<Task>()

    //region Fragment Methods

    override fun onCreate(savedInstanceState: Bundle?)
    {
        setHasOptionsMenu(true)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        when (item.itemId)
        {
            R.id.action_undo    -> undoDeleteTask()
            R.id.action_restore -> restoreAllCompletedTasks()
        }
        return super.onOptionsItemSelected(item)
    }

    //endregion

    //region Methods

    private fun initUI()
    {
        binding.fab.setOnClickListener()
        {
            navigate(R.id.action_taskListFragment_to_taskManagerFragment)
        }

        importanceStringArray = resources.getStringArray(R.array.frgTaskAdd_spnImportance_entries)

        initTaskRecyclerViewAdapter()

        presenter.getList()
    }

    private fun initTaskRecyclerViewAdapter()
    {
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

    private fun undoDeleteTask()
    {
        if (deletedTasks.isEmpty()) return

        val lastDeletedTask = deletedTasks.keys.last()
        val position = deletedTasks.values.last()

        presenter.undo(lastDeletedTask, position)
    }

    private fun restoreAllCompletedTasks()
    {
        if (completedTasks.isEmpty()) return

        presenter.restore(completedTasks)
    }

    //endregion

    //region TaskListContract.View

    override val isListEmpty: Boolean get() = taskAdapter.isEmpty

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

    override fun onGetListSuccess(listFromRepository: List<Task>)
    {
        val notCompletedTasksFromRepository = listFromRepository.filter { !it.isCompleted }
        val completedTasksFromRepository = listFromRepository.filter { it.isCompleted }

        taskAdapter.replaceList(notCompletedTasksFromRepository)

        completedTasks.clear()
        completedTasks.addAll(completedTasksFromRepository)
    }

    override fun onGetListFailure()
    {
        toast("There was an error when getting the tasks.")
    }

    override fun onNoData()
    {
        toast("The task list is empty.")
    }

    override fun onDeleteSuccess(deletedTask: Task, position: Int)
    {
        deletedTasks[deletedTask] = position

        taskAdapter.removeItem(deletedTask)

        toast("The task number $position was successfully deleted.")
    }

    override fun onDeleteFailure()
    {
        toast("There was an error when deleting.")
    }

    override fun onUndoSuccess(retrievedTask: Task, position: Int)
    {
        taskAdapter.insertItem(position, retrievedTask)

        deletedTasks.remove(retrievedTask)
    }

    override fun onUndoFailure()
    {
        toast("There was an error when undoing.")
    }

    override fun onCompletedStateChangedSuccess(completedStateChangedTask: Task, position: Int)
    {
        completedStateChangedTask.apply()
        {
            if (!isCompleted) return

            completedTasks.add(this)

            GlobalScope.launch(Dispatchers.Main)
            {
                delay(timeMillis = 450)
                taskAdapter.removeItem(this@apply)
            }
        }
    }

    override fun onCompletedStateChangedFailure()
    {
        toast("There was an error when changing the completed state.")
    }

    override fun onRestoreSuccess(notCompletedTasks: List<Task>)
    {
        taskAdapter.insertItems(0, notCompletedTasks)

        completedTasks.clear()
    }

    override fun onRestoreFailure()
    {
        toast("There was an error when restoring the completed tasks.")
    }

    //endregion

    //region RecyclerViewAdapter.OnBindViewHolderListener<>

    override fun onBindViewHolder(view: View, item: Task, position: Int)
    {
        ItemTaskBinding.bind(view).apply()
        {
            tvName.text = item.name
            tvImportance.text = importanceStringArray[item.importance.ordinal]
            chkIsCompleted.isChecked = item.isCompleted

            chkIsCompleted.setOnClickListener()
            {
                presenter.changeCompletedState(
                    taskToChangeCompletedState = item,
                    position = position,
                    newState = chkIsCompleted.isChecked
                )

                // In case the new state is not set then we have to also change
                // the state in the UI.
                chkIsCompleted.isChecked = item.isCompleted
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
        presenter.delete(selectedItem, position)

        return true
    }

    //endregion
}