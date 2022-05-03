package com.elian.taskproject.ui.taskmanager

import com.elian.taskproject.data.model.Task
import com.elian.taskproject.data.repository.TaskStaticRepository

class TaskManagerInteractor(private val listener: ITaskManagerContract.IOnInteractorListener) :
    ITaskManagerContract.IInteractor,
    ITaskManagerContract.IOnRepositoryCallback
{
    //region ITaskManagerContract.IInteractor

    override fun validateFields(task: Task): Boolean
    {
        var isError = false

        listener.apply()
        {
            if (task.name.isEmpty()) onNameEmptyError().let { isError = true }
            if (task.description.isEmpty()) onDescriptionEmptyError().let { isError = true }
            if (task.estimatedEndDate == null) onDateEmptyError().let { isError = true }
        }

        if (isError) onAddFailure()

        return !isError
    }

    override fun add(task: Task)
    {
        TaskStaticRepository.add(this, task)
    }

    override fun edit(editedTask: Task, position: Int)
    {
        TaskStaticRepository.edit(this, editedTask, position)
    }

    //endregion

    //region ITaskManagerContract.IOnRepositoryCallback

    override fun onAddSuccess()
    {
        listener.onAddSuccess()
    }

    override fun onEditSuccess()
    {
        listener.onEditSuccess()
    }

    override fun onAddFailure()
    {
        listener.onAddFailure()
    }

    override fun onEditFailure()
    {
        listener.onEditFailure()
    }

    //endregion
}