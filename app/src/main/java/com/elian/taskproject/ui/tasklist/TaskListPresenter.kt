package com.elian.taskproject.ui.tasklist

import com.elian.taskproject.data.model.Task

class TaskListPresenter(private var view: TaskListContract.View?) :
    TaskListContract.Presenter
{
    private var interactor: TaskListContract.Interactor? = TaskListInteractor(this)

    //region TaskListContract.Presenter

    override fun getList()
    {
        view?.showProgress()
        interactor?.getList()
    }

    override fun delete(taskToDelete: Task, position: Int)
    {
        interactor?.delete(taskToDelete, position)
    }

    override fun onDestroy()
    {
        view = null
        interactor = null
    }

    override fun onListSuccess(listFromRepository: List<Task>)
    {
        view?.hideProgress()
        view?.onListSuccess(listFromRepository)
    }

    override fun onListFailure()
    {
        view?.hideProgress()
        view?.onListFailure()
    }

    override fun onNoData()
    {
        view?.hideProgress()
        view?.onNoData()
        view?.showNoDataImage()
    }

    override fun onDeleteSuccess(deletedTask: Task, position: Int)
    {
        view?.onDeleteSuccess(deletedTask, position)

        view?.apply { if (isListEmpty) showNoDataImage() }
    }

    override fun onDeleteFailure()
    {
        view?.onDeleteFailure()
    }

    //endregion
}