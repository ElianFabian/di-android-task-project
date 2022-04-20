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
        var isError = false

        if (task.name.isEmpty()) listener.onNameEmptyError().let { isError = true }
        if (task.description.isEmpty()) listener.onDescriptionEmptyError().let { isError = true }
        if (task.endDateEstimated == null) listener.onDateEmptyError().let { isError = true }

        if (!isError) TaskStaticRepository.add(this, task)
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