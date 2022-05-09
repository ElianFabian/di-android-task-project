package com.elian.taskproject.data.repository

import com.elian.taskproject.data.TaskDatabase
import com.elian.taskproject.data.model.Task
import com.elian.taskproject.ui.taskmanager.ITaskManagerContract
import com.elian.taskproject.ui.tasklist.ITaskListContract

object TaskRepository :
    ITaskListContract.IRepository,
    ITaskManagerContract.IRepository
{
    //region ITaskListContract.IRepository

    override fun getList(callback: ITaskListContract.IOnRepositoryCallback)
    {
        if (TaskDatabase.getDatabase().taskDAO.selectAll().isEmpty())
        {
            callback.onNoData()
        }
        else callback.onListSuccess(TaskDatabase.getDatabase().taskDAO.selectAll())
    }

    override fun delete(callback: ITaskListContract.IOnRepositoryCallback, task: Task)
    {
        val taskList = TaskDatabase.getDatabase().taskDAO.selectAll()

        val position = taskList.indexOf(task)

        TaskDatabase.getDatabase().taskDAO.delete(task)
        callback.onDeleteSuccess(task, position)
    }

    //endregion

    //region ITaskAddContract.IRepository

    override fun add(callback: ITaskManagerContract.IOnRepositoryCallback, task: Task)
    {
        TaskDatabase.getDatabase().taskDAO.insert(task)
        callback.onAddSuccess()
    }

    override fun edit(callback: ITaskManagerContract.IOnRepositoryCallback, editedTask: Task, position: Int)
    {
        TaskDatabase.getDatabase().taskDAO.update(editedTask)
        callback.onEditSuccess()
    }

    //endregion
}
