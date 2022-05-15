package com.elian.taskproject.ui.tasklist

import com.elian.taskproject.base.BasePresenter
import com.elian.taskproject.data.model.Task

interface TaskListContract
{
    interface View : OnRepositoryCallback
    {
        fun showProgress()
        fun hideProgress()
        fun showNoDataImage()
        fun hideNoDataImage()
    }

    interface Presenter : BasePresenter
    {
        fun onGetList()
        fun onDelete(task: Task, position: Int)
    }

    interface Interactor
    {
        fun getList()
        fun delete(task: Task, position: Int)
    }

    interface Repository
    {
        fun getList(callback: OnRepositoryCallback)
        fun delete(callback: OnRepositoryCallback, task: Task, position: Int)
    }

    interface OnRepositoryCallback
    {
        fun onListSuccess(list: List<Task>)
        fun onListFailure()
        fun onNoData()

        fun onDeleteSuccess(deletedTask: Task, position: Int)
        fun onDeleteFailure()
    }

    interface OnInteractorListener : OnRepositoryCallback
}
