package com.elian.taskproject.ui.tasklist

import com.elian.taskproject.base.IBasePresenter
import com.elian.taskproject.data.model.Task

interface ITaskListContract
{
    interface IView : IOnRepositoryCallback
    {
        fun showProgress()
        fun hideProgress()
        fun showNoDataImage()
        fun hideNoDataImage()
    }

    interface IPresenter : IBasePresenter
    {
        fun getList()
        fun delete(task: Task)
    }

    interface IInteractor
    {
        fun getList()
        fun delete(task: Task)
    }

    interface IRepository
    {
        fun getList(callback: IOnRepositoryCallback)
        fun delete(callback: IOnRepositoryCallback, task: Task)
    }

    interface IOnRepositoryCallback
    {
        fun onListSuccess(list: List<Task>)
        fun onListFailure()
        fun onNoData()

        fun onDeleteSuccess(deletedTask: Task, position: Int)
    }

    interface IOnInteractorListener : IOnRepositoryCallback
}
