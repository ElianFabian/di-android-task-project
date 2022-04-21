package com.elian.taskproject.ui.tasklist

import com.elian.taskproject.data.model.Task
import com.elian.taskproject.data.repository.TaskStaticRepository

class TaskListInteractor(private val listener: ITaskListContract.IOnInteractorListener) :
    ITaskListContract.IInteractor,
    ITaskListContract.IOnRepositoryCallback
{
    //region ITaskListContract.IInteractor

    override fun getList()
    {
        TaskStaticRepository.getList(this)
    }

    //endregion

    //region ITaskListContract.IOnRepositoryCallback

    override fun onSuccess(list: List<Task>)
    {
        listener.onSuccess(list)
    }

    override fun onNoData()
    {
        listener.onNoData()
    }

    //endregion
}