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

    override fun delete(task: Task)
    {
        repository.delete(this, task)
    }

    //endregion

    //region TaskListContract.OnRepositoryCallback

    override fun onListSuccess(list: List<Task>)
    {
        listener.onListSuccess(list)
    }

    override fun onNoData()
    {
        listener.onNoData()
    }

    override fun onDeleteSuccess(deletedTask: Task, position: Int)
    {
        listener.onDeleteSuccess(deletedTask, position)
    }

    //endregion
}