package com.elian.taskproject.ui.tasklist

import com.elian.taskproject.data.model.Task
import com.elian.taskproject.data.repository.TaskRepository
import com.elian.taskproject.data.repository.TaskStaticRepository

class TaskListInteractor(private val listener: ITaskListContract.IOnInteractorListener) :
    ITaskListContract.IInteractor,
    ITaskListContract.IOnRepositoryCallback
{
    //region ITaskListContract.IInteractor

    override fun getList()
    {
        TaskRepository.getList(this)
    }

    override fun delete(task: Task)
    {
        TaskRepository.delete(this, task)
    }

    //endregion

    //region ITaskListContract.IOnRepositoryCallback

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