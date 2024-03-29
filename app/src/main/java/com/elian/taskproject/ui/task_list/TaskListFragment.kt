package com.elian.taskproject.ui.task_list

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elian.taskproject.R
import com.elian.taskproject.base.BaseFragment
import com.elian.taskproject.data.model.Task
import com.elian.taskproject.util.RecyclerViewAdapter
import com.elian.taskproject.databinding.FragmentTaskListBinding
import com.elian.taskproject.databinding.ItemTaskBinding
import com.elian.taskproject.util.ArgKeys
import com.elian.taskproject.util.ToggleAction
import com.elian.taskproject.util.extensions.navigate
import com.elian.taskproject.util.extensions.toast
import kotlinx.coroutines.*

class TaskListFragment : BaseFragment(),
    TaskListContract.View,
    RecyclerViewAdapter.OnBindViewHolderListener<Task>,
    RecyclerViewAdapter.OnItemClickListener<Task>,
    RecyclerViewAdapter.OnItemLongClickListener<Task>,
    MenuProvider
{
    override lateinit var presenter: TaskListContract.Presenter

    private lateinit var binding: FragmentTaskListBinding

    private val taskAdapter = RecyclerViewAdapter<Task>(R.layout.item_task)

    private lateinit var importanceStringArray: Array<String>

    private val deletedTasks = linkedMapOf<Task, Int>()
    private val completedTasks = mutableSetOf<Task>()

    private val sortByNameActions = ToggleAction(
        { presenter.sortByNameDescending() },
        { presenter.sortByNameAscending() }
    )

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

    //region MenuProvider

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater)
    {
        menuInflater.inflate(R.menu.menu_actions, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean
    {
        when (menuItem.itemId)
        {
            R.id.option_undo                   -> undo()
            R.id.option_markTasksAsUncompleted -> markTasksAsUncompleted()
            R.id.option_sortAlphabetically     -> sortByNameActions.toggle()
        }
        return false
    }

    //endregion

    //region Methods

    private fun initUI()
    {
        val menuHost = requireActivity() as MenuHost

        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

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
            putSerializable(ArgKeys.SelectedTask, task)
            putInt(ArgKeys.SelectedTaskPosition, position)
        })
    }

    private fun undo()
    {
        if (deletedTasks.isEmpty()) return

        val lastDeletedTask = deletedTasks.keys.last()
        val position = deletedTasks.values.last()

        presenter.undo(lastDeletedTask, position)
    }

    private fun markTasksAsUncompleted()
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
        val completedTasks = listFromRepository.filter { it.isCompleted }
        val uncompletedTasks = listFromRepository.filter { !it.isCompleted }

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

    override fun onSortByNameAscendingSuccess(uncompletedTasksSortedByNameAscending: List<Task>)
    {
        taskAdapter.replaceList(uncompletedTasksSortedByNameAscending)
    }

    override fun onSortByNameAscendingFailure()
    {
        toast("There was an error when sorting the tasks by name.")
    }

    override fun onSortByNameDescendingSuccess(uncompletedTasksSortedByNameDescending: List<Task>)
    {
        taskAdapter.replaceList(uncompletedTasksSortedByNameDescending)
    }

    override fun onSortByNameDescendingFailure()
    {
        toast("There was an error when sorting the tasks by name.")
    }

    //endregion
}