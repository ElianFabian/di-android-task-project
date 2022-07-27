package com.elian.taskproject.ui

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elian.taskproject.R
import com.elian.taskproject.data.model.Task
import com.elian.taskproject.util.RecyclerViewAdapter
import com.elian.taskproject.databinding.FragmentTaskListBinding
import com.elian.taskproject.databinding.ItemTaskBinding
import com.elian.taskproject.util.ArgKeys
import com.elian.taskproject.util.ToggleAction
import com.elian.taskproject.util.extensions.navigate
import com.elian.taskproject.view_model.TaskListViewModel

class TaskListFragment : Fragment(),
    RecyclerViewAdapter.OnBindViewHolderListener<Task>,
    RecyclerViewAdapter.OnItemClickListener<Task>,
    RecyclerViewAdapter.OnItemLongClickListener<Task>,
    MenuProvider
{
    private lateinit var binding: FragmentTaskListBinding

    private val viewModel: TaskListViewModel by viewModels()

    private val taskAdapter = RecyclerViewAdapter<Task>(R.layout.item_task)

    private lateinit var importanceStringArray: Array<String>

    private val sortByNameActions = ToggleAction(
        { viewModel.sortByNameDescending() },
        { viewModel.sortByNameAscending() }
    )

    //region Fragment Methods

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        initializeViewModel()
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

        initializeUI()
    }

    //endregion

    //region MenuProvider Methods

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater)
    {
        menuInflater.inflate(R.menu.menu_actions, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean
    {
        when (menuItem.itemId)
        {
            R.id.option_undo               -> viewModel.undo()
            R.id.option_uncheckTaskList    -> viewModel.uncheckTaskList()
            R.id.option_sortAlphabetically -> sortByNameActions.toggle()
        }
        return false
    }

    //endregion

    //region Methods

    private fun initializeUI()
    {
        val menuHost = requireActivity() as MenuHost

        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding.fab.setOnClickListener()
        {
            navigate(R.id.action_taskListFragment_to_taskManagerFragment)
        }

        importanceStringArray = resources.getStringArray(R.array.frgTaskAdd_spnImportance_entries)

        initializeTaskRecyclerViewAdapter()

        viewModel.getTaskList()
    }

    private fun initializeViewModel()
    {
        viewModel.setUncompletedTaskList(taskAdapter.itemList)

        viewModel.isLoading.observe(this, ::onLoadStateChanged)

        viewModel.apply()
        {
            onGetTaskList = ::onGetTaskList
            onDeleteTask = ::onDelete
            onUndoDeleteTask = ::onUndo
            onCheckTask = ::onCheckTask
            onUncheckTaskList = ::onUncheckTaskList
            onSortTaskListByName = ::onSortByName
            onTaskListStateChanged = ::onTaskListStateChanged
        }
    }

    private fun initializeTaskRecyclerViewAdapter()
    {
        binding.rvTasks.adapter = taskAdapter
        binding.rvTasks.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        taskAdapter.setOnBindViewHolderListener(this)
        taskAdapter.setOnItemClickListener(this)
        taskAdapter.setOnItemLongClickListener(this)
    }

    private fun sendTaskToTaskManager(task: Task, position: Int)
    {
        navigate(R.id.action_taskListFragment_to_taskManagerFragment, Bundle().apply()
        {
            putSerializable(ArgKeys.SelectedTask, task)
            putInt(ArgKeys.SelectedTaskPosition, position)
        })
    }

    //endregion

    //region RecyclerViewAdapter

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

                viewModel.checkTask(item, position)
            }
        }
    }

    override fun onItemClick(v: View?, selectedItem: Task, position: Int)
    {
        sendTaskToTaskManager(selectedItem, position)
    }

    override fun onItemLongClick(v: View?, selectedItem: Task, position: Int): Boolean
    {
        viewModel.delete(selectedItem, position)

        return true
    }

    //endregion

    //region Events

    private fun onGetTaskList(taskList: List<Task>)
    {
        val uncompletedTaskList = taskList.filter { !it.isCompleted }

        taskAdapter.replaceList(uncompletedTaskList)
    }

    private fun onDelete(deletedTask: Task, position: Int)
    {
        taskAdapter.removeItem(deletedTask)
    }

    private fun onUndo(retrievedTask: Task, position: Int)
    {
        taskAdapter.insertItem(position, retrievedTask)
    }

    private fun onCheckTask(completedTask: Task, position: Int)
    {
        taskAdapter.removeItem(completedTask)
    }

    private fun onUncheckTaskList(uncompletedTaskList: List<Task>)
    {
        taskAdapter.insertItems(0, uncompletedTaskList)
    }

    private fun onSortByName(taskListSortedByName: List<Task>)
    {
        taskAdapter.replaceList(taskListSortedByName)
    }

    private fun onLoadStateChanged(isLoading: Boolean)
    {
        binding.progressBar.isVisible = isLoading
    }

    private fun onTaskListStateChanged(isEmpty: Boolean)
    {
        binding.ivNoData.isVisible = isEmpty
    }

    //endregion
}
