package com.elian.myapplication.ui.tasklist

import com.elian.myapplication.data.model.Task

class TaskListPresenter(private var view: ITaskListContract.IView?) :
    ITaskListContract.IPresenter,
    ITaskListContract.IOnInteractorListener
{
    private var interactor: ITaskListContract.IInteractor = TaskListInteractor(this)

    //region ITaskListContract.IPresenter

    override fun load()
    {
        view?.showProgress()
        interactor.load()
    }

    override fun onDestroy()
    {
        view = null
    }

    //endregion

    //region IRepositoryListCallback

    override fun onSuccess(list: List<Task>)
    {
        view?.onSuccess(list)
    }

    override fun onNoData()
    {
        view?.onNoData()
    }

    //endregion
}