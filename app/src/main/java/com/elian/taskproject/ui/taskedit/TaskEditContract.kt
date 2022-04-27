package com.elian.taskproject.ui.taskedit

import com.elian.taskproject.base.IBasePresenter
import com.elian.taskproject.data.model.Task

interface ITaskEditContract
{
    interface IView : IOnRepositoryCallback

    interface IPresenter : IBasePresenter
    {
        fun onEdit(task: Task)
    }

    interface IInteractor
    {
        fun edit(task: Task)
    }

    interface IRepository
    {
        fun edit(callback: IOnRepositoryCallback, task: Task)
    }

    interface IOnRepositoryCallback
    {
        fun onSuccess()
        fun onFailure()
    }

    interface IOnInteractorListener : IOnRepositoryCallback
}