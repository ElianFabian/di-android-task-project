package com.elian.taskproject.ui.taskmanager

import com.elian.taskproject.data.model.Task
import com.elian.taskproject.data.repository.TaskRoomRepository

class TaskManagerInteractor(private val listener: TaskManagerContract.OnInteractorListener) :
    TaskManagerContract.Interactor,
    TaskManagerContract.OnRepositoryCallback
{
    private val repository: TaskManagerContract.Repository = TaskRoomRepository

    //region TaskManagerContract.Interactor

    override fun validateFields(task: Task): Boolean = when
    {
        task.name.isEmpty()        -> listener.onNameEmptyError().run { true }
        task.description.isEmpty() -> listener.onDescriptionEmptyError().run { true }
        task.endDate == null       -> listener.onDateEmptyError().run { true }

        else                       -> false
    }

    override fun add(task: Task)
    {
        repository.add(this, task)
    }

    override fun edit(editedTask: Task, position: Int)
    {
        repository.edit(this, editedTask, position)
    }

    override fun onAddSuccess()
    {
        listener.onAddSuccess()
    }

    override fun onAddFailure()
    {
        listener.onAddFailure()
    }

    override fun onEditSuccess()
    {
        listener.onEditSuccess()
    }

    override fun onEditFailure()
    {
        listener.onEditFailure()
    }

    //endregion
}