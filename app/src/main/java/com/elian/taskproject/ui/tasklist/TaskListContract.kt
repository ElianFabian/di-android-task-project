package com.elian.taskproject.ui.tasklist

import com.elian.taskproject.base.BasePresenter
import com.elian.taskproject.data.model.Task

interface TaskListContract
{
    interface View : OnRepositoryCallback
    {
        val isListEmpty: Boolean

        fun showProgress()
        fun hideProgress()

        fun showNoDataImage()
        fun hideNoDataImage()
    }

    interface Presenter : BasePresenter, Actions, OnInteractorListener

    interface Interactor : Actions, OnRepositoryCallback

    interface Repository
    {
        fun getList(callback: OnGetListCallback)
        fun delete(callback: OnDeleteCallback, taskToDelete: Task, position: Int)
        fun undo(callback: OnUndoCallback, taskToUndo: Task, position: Int)
    }


    interface Actions
    {
        fun getList()
        fun delete(taskToDelete: Task, position: Int)
        fun undo(taskToUndo: Task, position: Int)
    }

    interface OnInteractorListener : OnRepositoryCallback

    interface OnRepositoryCallback :
        OnGetListCallback,
        OnDeleteCallback,
        OnUndoCallback

    interface OnGetListCallback
    {
        fun onGetListSuccess(listFromRepository: List<Task>)
        fun onGetListFailure()
        fun onNoData()
    }

    interface OnDeleteCallback
    {
        fun onDeleteSuccess(deletedTask: Task, position: Int)
        fun onDeleteFailure()
    }

    interface OnUndoCallback
    {
        fun onUndoSuccess(taskToUndo: Task, position: Int)
        fun onUndoFailure()
    }
}
