package com.elian.taskproject.ui.tasklist

import com.elian.taskproject.data.model.Task
import com.elian.taskproject.data.repository.TaskRoomRepository

class TaskListInteractor(private val listener: TaskListContract.OnInteractorListener) :
    TaskListContract.Interactor,
    TaskListContract.OnRepositoryCallback
{
    private val repository: TaskListContract.Repository = TaskRoomRepository

    //region TaskListContract.Interactor

    override fun getList()
    {
        repository.getList(this)
    }

    override fun delete(taskToDelete: Task, position: Int)
    {
        repository.delete(this, taskToDelete, position)
    }

    //endregion

    //region TaskListContract.OnRepositoryCallback

    override fun onListSuccess(listFromRepository: List<Task>)
    {
        listener.onListSuccess(listFromRepository)
    }

    override fun onListFailure()
    {
        listener.onListFailure()
    }

    override fun onNoData()
    {
        listener.onNoData()
    }

    override fun onDeleteSuccess(deletedTask: Task, position: Int)
    {
        listener.onDeleteSuccess(deletedTask, position)
    }

    override fun onDeleteFailure()
    {
        listener.onDeleteFailure()
    }

    //endregion
}