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

    interface Presenter : BasePresenter, OnInteractorListener, Actions

    interface Interactor : OnRepositoryCallback, Actions

    interface Repository
    {
        fun getList(callback: OnRepositoryCallback)
        fun delete(callback: OnRepositoryCallback, taskToDelete: Task, position: Int)
    }

    interface OnInteractorListener : OnRepositoryCallback

    interface OnRepositoryCallback
    {
        fun onListSuccess(listFromRepository: List<Task>)
        fun onListFailure()
        fun onNoData()

        fun onDeleteSuccess(deletedTask: Task, position: Int)
        fun onDeleteFailure()
    }

    interface Actions
    {
        fun getList()
        fun delete(taskToDelete: Task, position: Int)
    }
}
