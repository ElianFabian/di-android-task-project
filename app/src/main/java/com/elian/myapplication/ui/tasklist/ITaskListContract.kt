package com.elian.myapplication.ui.tasklist

interface ITaskListContract
{
    interface IView
    {
        fun showProgress()
        fun hideProgress()
    }

    interface IPresenter
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
}
