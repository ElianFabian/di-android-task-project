package com.elian.taskproject.ui.task_manager

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

    interface Presenter : BasePresenter, Actions

    interface Interactor : Actions
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
        fun add(callback: OnAddCallback, taskToAdd: Task)
        fun edit(callback: OnEditCallback, editedTask: Task, position: Int)
    }


    interface Actions
    {
        fun add(taskToAdd: Task)
        fun edit(editedTask: Task, position: Int)
    }

    interface OnInteractorListener : OnRepositoryCallback
    {
        fun onNameEmptyError()
        fun onDescriptionEmptyError()
        fun onDateEmptyError()
    }

    interface OnRepositoryCallback :
        OnAddCallback,
        OnEditCallback

    interface OnAddCallback
    {
        fun onAddSuccess()
        fun onAddFailure()
    }

    interface OnEditCallback
    {
        fun onEditSuccess()
        fun onEditFailure()
    }
}
