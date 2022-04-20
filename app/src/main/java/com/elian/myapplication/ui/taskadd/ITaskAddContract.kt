package com.elian.myapplication.ui.taskadd

import com.elian.myapplication.data.model.Task

interface ITaskAddContract
{
    interface IView
    {
        fun setNameEmptyError()
        fun setDescriptionEmptyError()
        fun setDateEmptyError()
        fun cleanInputFieldsErrors()
    }

    interface IPresenter
    {
        
    }

    interface IInteractor
    {

    }

    interface IRepository
    {
        fun add(callback: IOnRepositoryCallback, task: Task)
    }

    interface IOnRepositoryCallback
    {
        fun onSuccess()
        fun onFailure()
    }

    interface IOnInteractorListener : IOnRepositoryCallback
}
