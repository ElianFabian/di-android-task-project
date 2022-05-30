package com.elian.taskproject.data.repository

import com.elian.taskproject.data.model.Task
import com.elian.taskproject.ui.taskmanager.TaskManagerContract
import com.elian.taskproject.ui.tasklist.TaskListContract

object TaskStaticRepository :
    TaskListContract.Repository,
    TaskManagerContract.Repository
{
    private val taskList = arrayListOf<Task>()

    //region TaskListContract.Repository

    override fun getList(callback: TaskListContract.OnGetListCallback)
    {
        if (taskList.isEmpty())
        {
            callback.onNoData()
        }
        else callback.onGetListSuccess(taskList)
    }

    override fun delete(callback: TaskListContract.OnDeleteCallback, taskToDelete: Task, position: Int)
    {
        taskList.remove(taskToDelete)
        callback.onDeleteSuccess(taskToDelete, position)
    }

    override fun undo(callback: TaskListContract.OnUndoCallback, taskToRetrieve: Task, position: Int)
    {
        taskList.add(position, taskToRetrieve)
        callback.onUndoSuccess(taskToRetrieve, position)
    }

    override fun changeCompletedState(
        callback: TaskListContract.OnCompletedStateChangedCallback,
        taskToChangeCompletedState: Task,
        position: Int,
        newState: Boolean,
    )
    {
        taskToChangeCompletedState.isCompleted = newState

        taskList[position] = taskToChangeCompletedState
        callback.onCompletedStateChangedSuccess(taskToChangeCompletedState, position)
    }

    override fun markAsUncompleted(callback: TaskListContract.OnMarkAsUncompletedCallback, completedTasks: List<Task>)
    {
        val uncompletedTasks = completedTasks.toList().onEach { task -> task.markAsUncompleted() }

        taskList.addAll(uncompletedTasks)
        callback.onMarkAsUncompletedSuccess(uncompletedTasks)
    }

    override fun sortByNameAscending(callback: TaskListContract.OnSortByNameAscendingCallback)
    {
        val sortedList = taskList.sortedBy { it.name }

        callback.onSortByNameAscendingSuccess(sortedList)
    }

    override fun sortByNameDescending(callback: TaskListContract.OnSortByNameDescendingCallback)
    {
        val sortedList = taskList.sortedByDescending { it.name }

        callback.onSortByNameDescendingSuccess(sortedList)
    }

    //endregion

    //region TaskManagerContract.Repository

    override fun add(callback: TaskManagerContract.OnAddCallback, task: Task)
    {
        taskList.add(task)
        callback.onAddSuccess()
    }

    override fun edit(callback: TaskManagerContract.OnEditCallback, editedTask: Task, position: Int)
    {
        taskList[position] = editedTask
        callback.onEditSuccess()
    }

    //endregion
}
