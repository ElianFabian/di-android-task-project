package com.elian.taskproject.ui.tasklist

import com.elian.taskproject.data.model.Task
import com.elian.taskproject.data.repository.TaskRoomRepository

class TaskListInteractor(private val listener: ITaskListContract.IOnInteractorListener) :
    ITaskListContract.IInteractor,
    ITaskListContract.IOnRepositoryCallback
{
    private val repository = TaskRoomRepository
    
    //region ITaskListContract.IInteractor

    override fun getList()
    {
        repository.getList(this)
    }

    override fun delete(task: Task)
    {
        repository.delete(this, task)
    }

    //endregion

    //region ITaskListContract.IOnRepositoryCallback

    override fun onListSuccess(list: List<Task>)
    {
        listener.onListSuccess(list)
    }

    override fun onListFailure()
    {
        //TODO("Not yet implemented")
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