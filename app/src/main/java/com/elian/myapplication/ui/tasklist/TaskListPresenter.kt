package com.elian.myapplication.ui.tasklist

import com.elian.myapplication.base.IRepositoryListCallback
import com.elian.myapplication.data.model.Task

class TaskListPresenter :
    ITaskListContract.IPresenter,
    IRepositoryListCallback
{
    //region ITaskListContract.IPresenter

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