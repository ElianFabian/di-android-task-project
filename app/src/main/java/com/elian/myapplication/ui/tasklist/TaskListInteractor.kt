package com.elian.myapplication.ui.tasklist

import com.elian.myapplication.data.model.Task
import com.elian.myapplication.data.repository.TaskStaticRepository

class TaskListInteractor(private val listener: ITaskListContract.IOnInteractorListener) :
    ITaskListContract.IInteractor,
    ITaskListContract.IOnRepositoryCallback
{
    //region ITaskListContract.IInteractor

    override fun load()
    {
        TaskStaticRepository.instance.getTaskList(this)
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