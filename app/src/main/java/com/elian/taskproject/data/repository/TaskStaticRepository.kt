package com.elian.taskproject.data.repository

import com.elian.taskproject.data.model.Task
import com.elian.taskproject.ui.taskmanager.ITaskManagerContract
import com.elian.taskproject.ui.tasklist.ITaskListContract

object TaskStaticRepository :
    ITaskListContract.IRepository,
    ITaskManagerContract.IRepository
{
    private val taskList = arrayListOf<Task>()

    //region ITaskListContract.IRepository

    override fun getList(callback: ITaskListContract.IOnRepositoryCallback)
    {
        if (taskList.isEmpty())
        {
            callback.onNoData()
        }
        else callback.onListSuccess(taskList)
    }

    override fun delete(callback: ITaskListContract.IOnRepositoryCallback, task: Task, position: Int)
    {
        taskList.remove(task)
        callback.onDeleteSuccess(task, position)
    }

    //endregion

    //region ITaskAddContract.IRepository

    override fun add(callback: ITaskManagerContract.IOnRepositoryCallback, task: Task)
    {
        taskList.add(task)
        callback.onAddSuccess()
    }

    override fun edit(callback: ITaskManagerContract.IOnRepositoryCallback, editedTask: Task, position: Int)
    {
        taskList[position] = editedTask
        callback.onEditSuccess()
    }

    //endregion
}
