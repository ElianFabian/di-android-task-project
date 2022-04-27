package com.elian.taskproject.ui.tasklist

import com.elian.taskproject.data.model.Task

class TaskListPresenter(private var view: ITaskListContract.IView?) :
    ITaskListContract.IPresenter,
    ITaskListContract.IOnInteractorListener
{
    private var interactor: ITaskListContract.IInteractor? = TaskListInteractor(this)

    //region ITaskListContract.IPresenter

    override fun getList()
    {
        view?.showProgress()
        interactor?.getList()
    }

    override fun delete(task: Task)
    {
        interactor?.delete(task)
    }

    override fun onDestroy()
    {
        view = null
        interactor = null
    }

    //endregion

    //region ITaskListContract.IOnInteractorListener

    override fun onListSuccess(list: List<Task>)
    {
        view?.onListSuccess(list)
        view?.hideProgress()
    }

    override fun onNoData()
    {
        view?.onNoData()
        view?.hideProgress()
    }

    override fun onDeleteSuccess(list: List<Task>)
    {
        view?.onDeleteSuccess(list)
    }

    //endregion
}