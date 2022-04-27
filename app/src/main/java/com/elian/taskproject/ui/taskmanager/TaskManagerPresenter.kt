package com.elian.taskproject.ui.taskmanager

import com.elian.taskproject.data.model.Task

class TaskManagerPresenter(private var view: ITaskManagerContract.IView?) :
    ITaskManagerContract.IPresenter,
    ITaskManagerContract.IOnInteractorListener
{
    private var interactor: ITaskManagerContract.IInteractor? = TaskManagerInteractor(this)

    //region ITaskManagerListContract.IPresenter

    override fun onValidateFields(task: Task)
    {
        view?.cleanInputFieldsErrors()
        interactor?.validateFields(task)
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

    override fun onSuccess()
    {
        view?.onSuccess()
    }

    override fun onError()
    {
        view?.onError()
    }

    //endregion
}