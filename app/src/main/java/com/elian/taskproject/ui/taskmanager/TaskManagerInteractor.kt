package com.elian.taskproject.ui.taskmanager

import com.elian.taskproject.data.model.Task
import com.elian.taskproject.data.repository.TaskFirebaseRepository
import com.elian.taskproject.data.repository.TaskRoomRepository
import com.elian.taskproject.data.repository.TaskStaticRepository

class TaskManagerInteractor(private val listener: ITaskManagerContract.IOnInteractorListener) :
    ITaskManagerContract.IInteractor,
    ITaskManagerContract.IOnRepositoryCallback
{
    private val repository: ITaskManagerContract.IRepository = TaskFirebaseRepository

    //region ITaskManagerContract.IInteractor

    override fun validateFields(task: Task): Boolean
    {
        listener.apply()
        {
            if (task.name.isEmpty()) onNameEmptyError().also { return true }
            if (task.description.isEmpty()) onDescriptionEmptyError().also { return true }
            if (task.estimatedEndDate == null) onDateEmptyError().also { return true }
        }

        return false
    }

    override fun add(task: Task)
    {
        repository.add(this, task)
    }

    override fun edit(editedTask: Task, position: Int)
    {
        repository.edit(this, editedTask, position)
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