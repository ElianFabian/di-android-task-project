package com.elian.taskproject.ui.taskmanager

import com.elian.taskproject.data.model.Task

class TaskManagerPresenter(private var view: ITaskManagerContract.IView?) :
    ITaskManagerContract.IPresenter,
    ITaskManagerContract.IOnInteractorListener
{
    private var interactor: ITaskManagerContract.IInteractor? = TaskManagerInteractor(this)

    //region ITaskManagerListContract.IPresenter

    override fun add(task: Task)
    {
        if (interactor?.validateFields(task) as Boolean)
        {
            interactor?.add(task)
        }
    }

    override fun edit(editedTask: Task, position: Int)
    {
        if (interactor?.validateFields(editedTask) as Boolean)
        {
            interactor?.edit(editedTask, position)
        }
    }

    override fun onDestroy()
    {
        view = null
        interactor = null
    }

    //endregion

    //region ITaskManagerContract.IOnInteractorListener

    override fun onNameEmptyError()
    {
        view?.setNameEmptyError()
    }

    override fun onDescriptionEmptyError()
    {
        view?.setDescriptionEmptyError()
    }

    override fun onDateEmptyError()
    {
        view?.setDateEmptyError()
    }

    override fun onAddSuccess()
    {
        view?.onAddSuccess()
    }

    override fun onEditSuccess()
    {
        view?.onEditSuccess()
    }

    override fun onAddError()
    {
        view?.onAddError()
    }

    override fun onEditError()
    {
        view?.onEditError()
    }

    //endregion
}