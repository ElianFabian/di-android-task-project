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

    override fun getList(callback: TaskListContract.OnRepositoryCallback)
    {
        if (taskList.isEmpty())
        {
            callback.onNoData()
        }
        else callback.onListSuccess(taskList)
    }

    override fun delete(callback: TaskListContract.OnRepositoryCallback, task: Task)
    {
        val position = taskList.indexOf(task)

        taskList.remove(task)
        callback.onDeleteSuccess(task, position)
    }

    //endregion

    //region ITaskAddContract.Repository

    override fun add(callback: TaskManagerContract.OnRepositoryCallback, task: Task)
    {
        taskList.add(task)
        callback.onAddSuccess()
    }

    override fun edit(callback: TaskManagerContract.OnRepositoryCallback, editedTask: Task, position: Int)
    {
        taskList[position] = editedTask
        callback.onEditSuccess()
    }

    //endregion
}
