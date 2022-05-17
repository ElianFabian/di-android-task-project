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

    interface Presenter : BasePresenter, OnInteractorListener, Actions

    interface Interactor : OnRepositoryCallback, Actions
    {
        /**
         * Validates the fields from the view given in a Task.
         *
         * @return true if there's no error.
         */
        fun validateFields(task: Task): Boolean
    }

    interface Repository
    {
        fun add(callback: OnRepositoryAddCallback, task: Task)
        fun edit(callback: OnRepositoryEditCallback, editedTask: Task, position: Int)
    }


    interface OnInteractorListener : OnRepositoryCallback
    {
        fun onNameEmptyError()
        fun onDescriptionEmptyError()
        fun onDateEmptyError()
    }

    interface OnRepositoryCallback :
        OnRepositoryAddCallback,
        OnRepositoryEditCallback

    interface OnRepositoryAddCallback
    {
        fun onAddSuccess()
        fun onAddFailure()
    }

    interface OnRepositoryEditCallback
    {
        fun onEditSuccess()
        fun onEditFailure()
    }

    interface Actions
    {
        fun add(task: Task)
        fun edit(editedTask: Task, position: Int)
    }
}
