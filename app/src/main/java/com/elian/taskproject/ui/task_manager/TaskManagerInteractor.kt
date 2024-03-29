package com.elian.taskproject.ui.task_manager

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

    override fun add(taskToAdd: Task)
    {
        repository.add(this, taskToAdd)
    }

    override fun edit(editedTask: Task, position: Int)
    {
        repository.edit(this, editedTask, position)
    }
    
    //endregion
    
    //region TaskManagerContract.OnRepositoryCallback

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