package com.elian.taskproject.data.repository

import com.elian.taskproject.data.database.AppDatabase
import com.elian.taskproject.data.model.Task
import com.elian.taskproject.ui.taskmanager.TaskManagerContract
import com.elian.taskproject.ui.tasklist.TaskListContract

object TaskRoomRepository :
    TaskListContract.Repository,
    TaskManagerContract.Repository
{
    private val taskDAO get() = AppDatabase.getDatabase().taskDAO

    //region TaskListContract.Repository

    override fun getList(callback: TaskListContract.OnRepositoryCallback)
    {
        if (taskDAO.selectAll().isEmpty())
        {
            callback.onNoData()
        }
        else callback.onListSuccess(taskDAO.selectAll())
    }

    override fun delete(callback: TaskListContract.OnRepositoryCallback, task: Task, position: Int)
    {
        taskDAO.delete(task)
        callback.onDeleteSuccess(task, position)
    }

    //endregion

    //region ITaskAddContract.Repository

    override fun add(callback: TaskManagerContract.OnRepositoryCallback, task: Task)
    {
        taskDAO.insert(task)
        callback.onAddSuccess()
    }

    override fun edit(callback: TaskManagerContract.OnRepositoryCallback, editedTask: Task, position: Int)
    {
        taskDAO.update(editedTask)
        callback.onEditSuccess()
    }

    //endregion
}
