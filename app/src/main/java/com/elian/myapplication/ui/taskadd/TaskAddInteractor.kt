package com.elian.myapplication.ui.taskadd

class TaskAddInteractor(private val listener: ITaskAddContract.IOnInteractorListener) :
    ITaskAddContract.IInteractor,
    ITaskAddContract.IOnRepositoryCallback
{
    //region ITaskAddContract.IInteractor

    override fun validateFields()
    {
        TODO("Not yet implemented")
    }

    //endregion

    //region IRepositoryListCallback


    //endregion

    override fun onSuccess()
    {
        TODO("Not yet implemented")
    }

    override fun onFailure()
    {
        TODO("Not yet implemented")
    }
}