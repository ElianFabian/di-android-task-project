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
        if (TaskDatabase.getDatabase().dao.selectAll().isEmpty())
        {
            callback.onNoData()
        }
        else callback.onListSuccess(TaskDatabase.getDatabase().dao.selectAll())
    }

    override fun delete(callback: ITaskListContract.IOnRepositoryCallback, task: Task)
    {
        val taskList = TaskDatabase.getDatabase().dao.selectAll()

        val position = taskList.indexOf(task)

        TaskDatabase.getDatabase().dao.delete(task)
        callback.onDeleteSuccess(task, position)
    }

    //endregion

    //region ITaskAddContract.IRepository

    override fun add(callback: ITaskManagerContract.IOnRepositoryCallback, task: Task)
    {
        TaskDatabase.getDatabase().dao.insert(task)
        callback.onAddSuccess()
    }

    override fun edit(callback: ITaskManagerContract.IOnRepositoryCallback, editedTask: Task, position: Int)
    {
        TaskDatabase.getDatabase().dao.update(editedTask)
        callback.onEditSuccess()
    }

    //endregion
}
