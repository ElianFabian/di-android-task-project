package com.elian.myapplication.ui.tasklist

import com.elian.myapplication.base.IBasePresenter
import com.elian.myapplication.data.model.Task

interface ITaskListContract
{
    interface IView
    {
        fun showProgress()
        fun hideProgress()
    }

    interface IPresenter : IBasePresenter
    {
        fun load()
    }

    interface IInteractor
    {
        fun load()
    }

    interface IRepository
    {
        fun load()
    }

    interface IRepositoryListCallback
    {
        fun onSuccess(list: List<Task>)
        fun onNoData()
    }

    interface IOnInteractorListener : IRepositoryListCallback
}
