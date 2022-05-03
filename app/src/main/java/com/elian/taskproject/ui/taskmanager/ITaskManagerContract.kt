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

        /**
         * Validates the fields from the view given in a Task.
         *
         * @return true if there's no error.
         */
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

        fun onAddFailure()
        fun onEditFailure()
    }

    interface IOnInteractorListener : IOnRepositoryCallback
    {
        fun onNameEmptyError()
        fun onDescriptionEmptyError()
        fun onDateEmptyError()
    }
}
