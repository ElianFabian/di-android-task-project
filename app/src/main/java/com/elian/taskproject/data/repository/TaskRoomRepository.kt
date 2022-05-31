package com.elian.taskproject.data.repository

import com.elian.taskproject.data.database.AppDatabase
import com.elian.taskproject.data.model.Task
import com.elian.taskproject.ui.tasklist.TaskListContract
import com.elian.taskproject.ui.taskmanager.TaskManagerContract
import java.util.concurrent.Callable
import java.util.concurrent.Future

object TaskRoomRepository :
    TaskListContract.Repository,
    TaskManagerContract.Repository
{
    private val taskDAO get() = AppDatabase.instance.taskDAO

    private val execute = AppDatabase.executorService::execute

    private fun <T> submit(callable: Callable<T>): Future<T>
    {
        return AppDatabase.executorService.submit(callable)
    }

    //region TaskListContract.Repository

    override fun getList(callback: TaskListContract.OnGetListCallback)
    {
        val list = submit { taskDAO.selectAll() }.get()

        if (list.isEmpty())
        {
            callback.onNoData()
        }
        else callback.onGetListSuccess(list)
    }

    override fun delete(callback: TaskListContract.OnDeleteCallback, taskToDelete: Task, position: Int)
    {
        execute { taskDAO.delete(taskToDelete) }

        callback.onDeleteSuccess(taskToDelete, position)
    }

    override fun undo(callback: TaskListContract.OnUndoCallback, taskToRetrieve: Task, position: Int)
    {
        execute { taskDAO.insert(taskToRetrieve) }

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

        execute { taskDAO.update(taskToChangeCompletedState) }

        callback.onCompletedStateChangedSuccess(taskToChangeCompletedState, position)
    }

    override fun markAsUncompleted(callback: TaskListContract.OnMarkAsUncompletedCallback, completedTasks: List<Task>)
    {
        val uncompletedTasks = completedTasks.toList().onEach { task -> task.markAsUncompleted() }

        execute { taskDAO.updateAll(uncompletedTasks) }

        callback.onMarkAsUncompletedSuccess(uncompletedTasks)
    }

    override fun sortByNameAscending(callback: TaskListContract.OnSortByNameAscendingCallback)
    {
        val list = submit { taskDAO.selectAll() }.get()

        val sortedList = list.filter { !it.isCompleted }.sortedBy { it.name }

        callback.onSortByNameAscendingSuccess(sortedList)
    }

    override fun sortByNameDescending(callback: TaskListContract.OnSortByNameDescendingCallback)
    {
        val list = submit { taskDAO.selectAll() }.get()

        val sortedList = list.filter { !it.isCompleted }.sortedByDescending { it.name }

        callback.onSortByNameDescendingSuccess(sortedList)
    }

    //endregion

    //region TaskManagerContract.Repository

    override fun add(callback: TaskManagerContract.OnAddCallback, task: Task)
    {
        execute { taskDAO.insert(task) }

        callback.onAddSuccess()
    }

    override fun edit(callback: TaskManagerContract.OnEditCallback, editedTask: Task, position: Int)
    {
        execute { taskDAO.update(editedTask) }

        callback.onEditSuccess()
    }

    //endregion
}
