package com.elian.taskproject.data.repository

import com.elian.taskproject.data.database.AppDatabase
import com.elian.taskproject.data.model.Task
import com.elian.taskproject.ui.tasklist.ITaskListContract
import com.elian.taskproject.ui.taskmanager.ITaskManagerContract

object TaskFirebaseRepository :
    ITaskListContract.IRepository,
    ITaskManagerContract.IRepository
{
    //region ITaskListContract.IRepository

    override fun getList(callback: ITaskListContract.IOnRepositoryCallback)
    {

    }

    override fun delete(callback: ITaskListContract.IOnRepositoryCallback, task: Task)
    {

    }

    //endregion

    //region ITaskAddContract.IRepository

    override fun add(callback: ITaskManagerContract.IOnRepositoryCallback, task: Task)
    {

    }

    override fun edit(callback: ITaskManagerContract.IOnRepositoryCallback, editedTask: Task, position: Int)
    {

    }

    //endregion
}