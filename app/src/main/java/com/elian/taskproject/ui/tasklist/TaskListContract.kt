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
        fun onDelete(task: Task)
    }

    interface Interactor
    {
        fun getList()
        fun delete(task: Task)
    }

    interface Repository
    {
        fun getList(callback: OnRepositoryCallback)
        fun delete(callback: OnRepositoryCallback, task: Task)
    }

    interface OnRepositoryCallback
    {
        fun onListSuccess(list: List<Task>)
        fun onNoData()

        fun onDeleteSuccess(deletedTask: Task, position: Int)
    }

    interface OnInteractorListener : OnRepositoryCallback
}
