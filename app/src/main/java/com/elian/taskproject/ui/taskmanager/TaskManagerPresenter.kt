package com.elian.taskproject.ui.taskmanager

import com.elian.taskproject.data.model.Task

class TaskManagerPresenter(private var view: TaskManagerContract.View?) :
    TaskManagerContract.Presenter,
    TaskManagerContract.OnInteractorListener
{
    private var interactor: TaskManagerContract.Interactor? = TaskManagerInteractor(this)

    //region ITaskManagerListContract.Presenter

    override fun onAdd(task: Task)
    {
        if (interactor!!.validateFields(task))
        {
            view?.onAddFailure()
        }
        else interactor?.add(task)
    }

    override fun onEdit(editedTask: Task, position: Int)
    {
        if (interactor!!.validateFields(editedTask))
        {
            view?.onEditFailure()
        }
        else interactor?.edit(editedTask, position)
    }

    override fun onDestroy()
    {
        view = null
        interactor = null
    }

    //endregion

    //region TaskManagerContract.OnInteractorListener

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

    override fun onAddFailure()
    {
        view?.onAddFailure()
    }

    override fun onEditFailure()
    {
        view?.onEditFailure()
    }

    //endregion
}