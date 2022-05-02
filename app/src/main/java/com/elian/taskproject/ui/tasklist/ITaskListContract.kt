package com.elian.taskproject.ui.tasklist

import com.elian.taskproject.base.IBasePresenter
import com.elian.taskproject.data.model.Task

interface ITaskListContract
{
    interface IView : IOnRepositoryCallback
    {
        fun showProgress()
        fun hideProgress()
    }

    interface IPresenter : IBasePresenter
    {
        fun getList()
        fun remove(task: Task)
    }

    interface IInteractor
    {
        fun getList()
        fun remove(task: Task)
    }

    interface IRepository
    {
        fun getList(callback: IOnRepositoryCallback)
        fun remove(callback: IOnRepositoryCallback, task: Task)
    }

    interface IOnRepositoryCallback
    {
        fun onListSuccess(list: List<Task>)
        fun onNoData()
        
        fun onRemoveSuccess(removedTask: Task)
    }

    interface IOnInteractorListener : IOnRepositoryCallback
}
