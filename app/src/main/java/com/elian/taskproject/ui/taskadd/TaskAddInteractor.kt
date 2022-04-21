package com.elian.taskproject.ui.taskadd

import com.elian.taskproject.data.model.Task
import com.elian.taskproject.data.repository.TaskStaticRepository

class TaskAddInteractor(private val listener: ITaskAddContract.IOnInteractorListener) :
    ITaskAddContract.IInteractor,
    ITaskAddContract.IOnRepositoryCallback
{
    //region ITaskAddContract.IInteractor

    override fun validateFields(task: Task)
    {
        var isError = false

        listener.apply()
        {
            if (task.name.isEmpty()) onNameEmptyError().let { isError = true }
            if (task.description.isEmpty()) onDescriptionEmptyError().let { isError = true }
            if (task.endDateEstimated == null) onDateEmptyError().let { isError = true }
        }

        if (!isError) TaskStaticRepository.add(this, task)
        else onFailure()
    }

    //endregion

    //region ITaskAddContract.IOnRepositoryCallback

    override fun onSuccess()
    {
        listener.onSuccess()
    }

    override fun onFailure()
    {
        listener.onFailure()
    }

    //endregion
}