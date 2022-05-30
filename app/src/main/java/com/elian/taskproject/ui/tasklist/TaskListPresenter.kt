package com.elian.taskproject.ui.tasklist

import com.elian.taskproject.data.model.Task

class TaskListPresenter(private var view: TaskListContract.View?) :
    TaskListContract.Presenter,
    TaskListContract.OnInteractorListener
{
    private var interactor: TaskListContract.Interactor? = TaskListInteractor(this)

    //region TaskListContract.Presenter

    override fun getList()
    {
        view?.showProgress()
        interactor?.getList()
        view?.hideProgress()
    }

    override fun delete(taskToDelete: Task, position: Int)
    {
        interactor?.delete(taskToDelete, position)
    }

    override fun undo(taskToRetrieve: Task, position: Int)
    {
        interactor?.undo(taskToRetrieve, position)
    }

    override fun changeCompletedState(taskToChangeCompletedState: Task, position: Int, newState: Boolean)
    {
        interactor?.changeCompletedState(taskToChangeCompletedState, position, newState)
    }

    override fun markAsUncompleted(completedTasks: List<Task>)
    {
        interactor?.markAsUncompleted(completedTasks)
    }

    override fun sortByNameAscending()
    {
        interactor?.sortByNameAscending()
    }

    override fun sortByNameDescending()
    {
        interactor?.sortByNameDescending()
    }

    override fun onDestroy()
    {
        view = null
        interactor = null
    }

    //endregion

    //region TaskListContract.OnInteractorListener

    override fun onGetListSuccess(listFromRepository: List<Task>)
    {
        view?.onGetListSuccess(listFromRepository)
    }

    override fun onGetListFailure()
    {
        view?.onGetListFailure()
    }

    override fun onNoData()
    {
        view?.showNoDataImage()
        view?.onNoData()
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

    override fun onUndoSuccess(retrievedTask: Task, position: Int)
    {
        view?.onUndoSuccess(retrievedTask, position)

        view?.apply { if (!isListEmpty) hideNoDataImage() }
    }

    override fun onUndoFailure()
    {
        view?.onUndoFailure()
    }

    override fun onCompletedStateChangedSuccess(completedStateChangedTask: Task, position: Int)
    {
        view?.onCompletedStateChangedSuccess(completedStateChangedTask, position)
    }

    override fun onCompletedStateChangedFailure()
    {
        view?.onCompletedStateChangedFailure()
    }

    override fun onMarkAsUncompletedSuccess(tasksMarkedAsUncompleted: List<Task>)
    {
        view?.onMarkAsUncompletedSuccess(tasksMarkedAsUncompleted)
    }

    override fun onMarkAsUncompletedFailure()
    {
        view?.onMarkAsUncompletedFailure()
    }

    override fun onSortByNameAscendingSuccess(tasksSortedByNameAscending: List<Task>)
    {
        view?.onSortByNameAscendingSuccess(tasksSortedByNameAscending)
    }

    override fun onSortByNameAscendingFailure()
    {
        view?.onSortByNameAscendingFailure()
    }

    override fun onSortByNameDescendingSuccess(tasksSortedByNameDescending: List<Task>)
    {
        view?.onSortByNameDescendingSuccess(tasksSortedByNameDescending)
    }

    override fun onSortByNameDescendingFailure()
    {
        view?.onSortByNameDescendingFailure()
    }

    //endregion
}