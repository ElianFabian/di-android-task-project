package com.elian.taskproject.ui.taskmanager

import com.elian.taskproject.base.BasePresenter
import com.elian.taskproject.data.model.Task

interface TaskManagerContract
{
    interface View : OnRepositoryCallback
    {
        fun setNameEmptyError()
        fun setDescriptionEmptyError()
        fun setDateEmptyError()
        fun cleanInputFieldsErrors()
    }

    interface Presenter : BasePresenter
    {
        fun add(task: Task)
        fun edit(editedTask: Task, position: Int)
    }

    interface Interactor
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

    interface Repository
    {
        fun add(callback: OnRepositoryCallback, task: Task)
        fun edit(callback: OnRepositoryCallback, editedTask: Task, position: Int)
    }

    interface OnRepositoryCallback
    {
        fun onAddSuccess()
        fun onEditSuccess()

        fun onAddFailure()
        fun onEditFailure()
    }

    interface OnInteractorListener : OnRepositoryCallback
    {
        fun onNameEmptyError()
        fun onDescriptionEmptyError()
        fun onDateEmptyError()
    }
}
