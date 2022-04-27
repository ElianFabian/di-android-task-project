package com.elian.taskproject.ui.taskedit

import com.elian.taskproject.base.IBasePresenter
import com.elian.taskproject.data.model.Task

interface ITaskEditContract
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
        fun onValidateFields(task: Task)
    }

    interface IRepository
    {
        fun edit(callback: IOnRepositoryCallback, task: Task)
    }

    interface IOnRepositoryCallback
    {
        fun onSuccess()
        fun onError()
    }

    interface IOnInteractorListener : IOnRepositoryCallback
    {
        fun onNameEmptyError()
        fun onDescriptionEmptyError()
        fun onDateEmptyError()
    }
}