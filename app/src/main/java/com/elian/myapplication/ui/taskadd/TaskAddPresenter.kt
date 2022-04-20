package com.elian.myapplication.ui.taskadd

import com.elian.myapplication.data.model.Task

class TaskAddPresenter(private var view: ITaskAddContract.IView?) :
    ITaskAddContract.IPresenter,
    ITaskAddContract.IOnInteractorListener
{
    private var interactor: ITaskAddContract.IInteractor? = TaskAddInteractor(this)

    //region ITaskAddListContract.IPresenter

    override fun onValidateFields(task: Task)
    {
        interactor?.validateFields(task)
    }

    override fun onDestroy()
    {
        view = null
        interactor = null
    }

    //endregion

    //region ITaskAddContract.IOnInteractorListener

    override fun onSuccess()
    {
        TODO("Not yet implemented")
    }

    override fun onFailure()
    {
        TODO("Not yet implemented")
    }

    //endregion
}