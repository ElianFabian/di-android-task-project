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

    override fun getList(callback: TaskListContract.OnRepositoryGetListCallback)
    {
        if (taskList.isEmpty())
        {
            callback.onNoData()
        }
        else callback.onListSuccess(taskList)
    }

    override fun delete(callback: TaskListContract.OnRepositoryDeleteCallback, taskToDelete: Task, position: Int)
    {
        taskList.remove(taskToDelete)
        callback.onDeleteSuccess(taskToDelete, position)
    }

    //endregion

    //region ITaskAddContract.Repository

    override fun add(callback: TaskManagerContract.OnRepositoryAddCallback, task: Task)
    {
        taskList.add(task)
        callback.onAddSuccess()
    }

    override fun edit(callback: TaskManagerContract.OnRepositoryEditCallback, editedTask: Task, position: Int)
    {
        taskList[position] = editedTask
        callback.onEditSuccess()
    }

    //endregion
}
