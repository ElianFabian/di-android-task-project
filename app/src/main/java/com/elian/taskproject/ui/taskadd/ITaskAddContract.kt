package com.elian.taskproject.ui.taskadd

import com.elian.taskproject.base.IBasePresenter
import com.elian.taskproject.data.model.Task

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
    {
        fun onNameEmptyError()
        fun onDescriptionEmptyError()
        fun onDateEmptyError()
    }
}
