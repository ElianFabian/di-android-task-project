package com.elian.myapplication.ui.taskadd

import com.elian.myapplication.data.model.Task
import com.elian.myapplication.data.repository.TaskStaticRepository

class TaskAddInteractor(private val listener: ITaskAddContract.IOnInteractorListener) :
    ITaskAddContract.IInteractor,
    ITaskAddContract.IOnRepositoryCallback
{
    //region ITaskAddContract.IInteractor

    override fun validateFields(task: Task)
    {
        with(task)
        {
            if (name.isEmpty() || description.isEmpty())
            {
                // TODO
                //onFailure()
                return
            }
        }

        TaskStaticRepository.add(this, task)
    }

    //endregion

    //region IRepositoryListCallback


    //endregion

    override fun onSuccess()
    {
        listener.onSuccess()
    }

    override fun onFailure()
    {
        listener.onFailure()
    }
}