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
            if (task.estimatedEndDate == null) onDateEmptyError().let { isError = true }
        }

        if (isError) onError()
        else TaskStaticRepository.add(this, task)
    }

    //endregion

    //region ITaskAddContract.IOnRepositoryCallback

    override fun onSuccess()
    {
        listener.onSuccess()
    }

    override fun onError()
    {
        listener.onError()
    }

    //endregion
}