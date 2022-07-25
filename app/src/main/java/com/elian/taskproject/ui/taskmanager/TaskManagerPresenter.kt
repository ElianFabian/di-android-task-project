package com.elian.taskproject.ui.taskmanager

import com.elian.taskproject.data.model.Task

class TaskManagerPresenter(private var view: TaskManagerContract.View?) :
    TaskManagerContract.Presenter,
    TaskManagerContract.OnInteractorListener
{
    private var interactor: TaskManagerContract.Interactor? = TaskManagerInteractor(this)

    //region TaskManagerListContract.Presenter

    override fun add(taskToAdd: Task)
    {
        if (interactor!!.validateFields(taskToAdd)) return

        interactor?.add(taskToAdd)
    }

    override fun edit(editedTask: Task, position: Int)
    {
        if (interactor!!.validateFields(editedTask)) return

        interactor?.edit(editedTask, position)
    }

    override fun onDestroy()
    {
        view = null
        interactor = null
    }

    //endregion

    //region TaskManagerInteractorListener

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

    override fun onAddFailure()
    {
        view?.onAddFailure()
    }

    override fun onEditSuccess()
    {
        view?.onEditSuccess()
    }

    override fun onEditFailure()
    {
        view?.onEditFailure()
    }

    //endregion
}