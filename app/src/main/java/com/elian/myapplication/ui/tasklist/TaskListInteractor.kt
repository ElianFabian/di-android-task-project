package com.elian.myapplication.ui.tasklist

import com.elian.myapplication.data.model.Task

class TaskListInteractor(private val listener: ITaskListContract.IOnInteractorListener) :
    ITaskListContract.IInteractor,
    ITaskListContract.IOnRepositoryCallback
{
    //region ITaskListContract.IInteractor

    override fun load()
    {
        TODO("Not yet implemented")
    }

    //endregion

    //region IRepositoryListCallback

    override fun onSuccess(list: List<Task>)
    {
        TODO("Not yet implemented")
    }

    override fun onNoData()
    {
        TODO("Not yet implemented")
    }

    //endregion

}