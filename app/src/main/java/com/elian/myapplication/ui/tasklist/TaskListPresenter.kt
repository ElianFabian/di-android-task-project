package com.elian.myapplication.ui.tasklist

import com.elian.myapplication.data.model.Task

class TaskListPresenter(private var view: ITaskListContract.IView) :
    ITaskListContract.IPresenter,
    ITaskListContract.IOnInteractorListener
{
    private var interactor: ITaskListContract.IInteractor = TaskListInteractor(this)

    //region ITaskListContract.IPresenter

    override fun load()
    {
        TODO("Not yet implemented")
    }

    override fun onDestroy()
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