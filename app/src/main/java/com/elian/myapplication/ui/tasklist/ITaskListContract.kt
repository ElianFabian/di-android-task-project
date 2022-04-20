package com.elian.myapplication.ui.tasklist

import com.elian.myapplication.base.IBasePresenter

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

    interface IOnInteractorListener : IRepositoryListCallback
}
