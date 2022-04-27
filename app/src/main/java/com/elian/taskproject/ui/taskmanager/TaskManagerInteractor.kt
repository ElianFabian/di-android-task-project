package com.elian.taskproject.ui.taskmanager

import com.elian.taskproject.data.model.Task
import com.elian.taskproject.data.repository.TaskStaticRepository

class TaskManagerInteractor(private val listener: ITaskManagerContract.IOnInteractorListener) :
    ITaskManagerContract.IInteractor,
    ITaskManagerContract.IOnRepositoryCallback
{
    //region ITaskManagerContract.IInteractor

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

    //region ITaskManagerContract.IOnRepositoryCallback

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