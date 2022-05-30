package com.elian.taskproject.ui.tasklist

import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elian.taskproject.R
import com.elian.taskproject.base.BaseFragment
import com.elian.taskproject.data.model.Task
import com.elian.taskproject.util.RecyclerViewAdapter
import com.elian.taskproject.databinding.FragmentTaskListBinding
import com.elian.taskproject.databinding.ItemTaskBinding
import com.elian.taskproject.util.ToggleAction
import com.elian.taskproject.util.extensions.navigate
import com.elian.taskproject.util.extensions.toast
import kotlinx.coroutines.*

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
    private val completedTasks = mutableSetOf<Task>()

    private val sortActions = ToggleAction(
        { presenter.sortByNameAscending() },
        { presenter.sortByNameDescending() }
    )

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        inflater.inflate(R.menu.menu_actions, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        when (item.itemId)
        {
            R.id.option_undo                  -> undoDeleteTask()
            R.id.option_restoreCompletedTasks -> restoreCompletedTasks()
            R.id.option_sort_alphabetically   -> sortActions.toggle()
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

    private fun restoreCompletedTasks()
    {
        if (completedTasks.isEmpty()) return

        presenter.markAsUncompleted(completedTasks.toList())
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
                // When the task is checked but it's not already gone from the recycler view
                // we make sure the user can't uncheck the task or even click it to edit it or delete it.
                view.setOnClickListener(null)
                view.setOnLongClickListener(null)
                chkIsCompleted.isClickable = false

                presenter.changeCompletedState(
                    taskToChangeCompletedState = item,
                    position = position,
                    newState = chkIsCompleted.isChecked
                )
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
        val uncompletedTasks = listFromRepository.filter { !it.isCompleted }
        val completedTasks = listFromRepository.filter { it.isCompleted }

        taskAdapter.replaceList(uncompletedTasks)

        this.completedTasks.clear()
        this.completedTasks.addAll(completedTasks)
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
        lifecycleScope.launch()
        {
            delay(450)

            completedTasks.add(completedStateChangedTask)
            taskAdapter.removeItem(completedStateChangedTask)
        }
    }

    override fun onCompletedStateChangedFailure()
    {
        toast("There was an error when changing the completed state.")
    }

    override fun onMarkAsUncompletedSuccess(tasksMarkedAsUncompleted: List<Task>)
    {
        taskAdapter.insertItems(0, tasksMarkedAsUncompleted)

        completedTasks.clear()
    }

    override fun onMarkAsUncompletedFailure()
    {
        toast("There was an error when restoring the completed tasks.")
    }

    override fun onSortByNameAscendingSuccess(tasksSortedByNameAscending: List<Task>)
    {
        taskAdapter.replaceList(tasksSortedByNameAscending)
    }

    override fun onSortByNameAscendingFailure()
    {
        toast("There was an error when sorting the tasks by name.")
    }

    override fun onSortByNameDescendingSuccess(tasksSortedByNameDescending: List<Task>)
    {
        taskAdapter.replaceList(tasksSortedByNameDescending)
    }

    override fun onSortByNameDescendingFailure()
    {
        toast("There was an error when sorting the tasks by name.")
    }

    //endregion
}