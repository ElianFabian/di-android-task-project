package com.elian.taskproject.data.repository

import com.elian.taskproject.data.database.AppDatabase
import com.elian.taskproject.data.model.Task
import com.elian.taskproject.ui.taskmanager.ITaskManagerContract
import com.elian.taskproject.ui.tasklist.ITaskListContract

object TaskRoomRepository :
    ITaskListContract.IRepository,
    ITaskManagerContract.IRepository
{
    private val taskDAO get() = AppDatabase.getDatabase().taskDAO

    //region ITaskListContract.IRepository

    override fun getList(callback: ITaskListContract.IOnRepositoryCallback)
    {
        if (taskDAO.selectAll().isEmpty())
        {
            callback.onNoData()
        }
        else callback.onListSuccess(taskDAO.selectAll())
    }

    override fun delete(callback: ITaskListContract.IOnRepositoryCallback, task: Task, position: Int)
    {
        taskDAO.delete(task)
        callback.onDeleteSuccess(task, position)
    }

    //endregion

    //region ITaskAddContract.IRepository

    override fun add(callback: ITaskManagerContract.IOnRepositoryCallback, task: Task)
    {
        taskDAO.insert(task)
        callback.onAddSuccess()
    }

    override fun edit(callback: ITaskManagerContract.IOnRepositoryCallback, editedTask: Task, position: Int)
    {
        taskDAO.update(editedTask)
        callback.onEditSuccess()
    }

    //endregion
}
