package com.elian.taskproject.ui.tasklist

import com.elian.taskproject.base.IBasePresenter
import com.elian.taskproject.data.model.Task

interface ITaskListContract
{
    interface IView : IOnRepositoryCallback
    {
        fun showProgress()
        fun hideProgress()
    }

    interface IPresenter : IBasePresenter
    {
        fun loadData()
    }

    interface IInteractor
    {
        fun loadData()
    }

    interface IRepository
    {
        fun getList(callback: IOnRepositoryCallback)
    }

    interface IOnRepositoryCallback
    {
        fun onSuccess(list: List<Task>)
        fun onNoData()
    }

    interface IOnInteractorListener : IOnRepositoryCallback
}
