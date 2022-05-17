package com.elian.taskproject.ui.tasklist

import com.elian.taskproject.data.model.Task

class TaskListPresenter(private var view: TaskListContract.View?) :
    TaskListContract.Presenter,
    TaskListContract.OnInteractorListener
{
    private var interactor: TaskListContract.Interactor? = TaskListInteractor(this)

    //region TaskListContract.Presenter

    override fun onGetList()
    {
        view?.showProgress()
        interactor?.getList()
    }

    override fun onDelete(taskToDelete: Task, position: Int)
    {
        interactor?.delete(taskToDelete, position)
    }

    override fun onDestroy()
    {
        view = null
        interactor = null
    }

    //endregion

    //region TaskListContract.OnInteractorListener

    override fun onListSuccess(list: List<Task>)
    {
        view?.onListSuccess(list)
        view?.hideProgress()
    }

    override fun onListFailure()
    {
        view?.onListFailure()
        view?.hideProgress()
    }

    override fun onNoData()
    {
        view?.onNoData()
        view?.hideProgress()
    }

    override fun onDeleteSuccess(deletedTask: Task, position: Int)
    {
        view?.onDeleteSuccess(deletedTask, position)
    }

    override fun onDeleteFailure()
    {
        view?.onDeleteFailure()
    }

    //endregion
}