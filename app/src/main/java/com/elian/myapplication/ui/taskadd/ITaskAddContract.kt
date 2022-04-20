package com.elian.myapplication.ui.taskadd

import com.elian.myapplication.base.IBasePresenter
import com.elian.myapplication.data.model.Task

interface ITaskAddContract
{
    interface IView : IOnRepositoryCallback
    {
        fun setNameEmptyError()
        fun setDescriptionEmptyError()
        fun setDateEmptyError()
        fun cleanInputFieldsErrors()
    }

    interface IPresenter : IBasePresenter
    {
        fun onValidateFields(task: Task)
    }

    interface IInteractor
    {
        fun validateFields(task: Task)
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
