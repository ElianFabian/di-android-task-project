package com.elian.taskproject.ui.taskmanager

import com.elian.taskproject.base.IBasePresenter
import com.elian.taskproject.data.model.Task

interface ITaskManagerContract
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
        fun add(task: Task)
        fun edit(editedTask: Task, position: Int)
    }

    interface IInteractor
    {
        fun add(task: Task)
        fun edit(editedTask: Task, position: Int)
        fun validateFields(task: Task): Boolean
    }

    interface IRepository
    {
        fun add(callback: IOnRepositoryCallback, task: Task)
        fun edit(callback: IOnRepositoryCallback, editedTask: Task, position: Int)
    }

    interface IOnRepositoryCallback
    {
        fun onAddSuccess()
        fun onEditSuccess()
        fun onAddError()
        fun onEditError()
    }

    interface IOnInteractorListener : IOnRepositoryCallback
    {
        fun onNameEmptyError()
        fun onDescriptionEmptyError()
        fun onDateEmptyError()
    }
}
