package com.elian.taskproject.ui.tasklist

import com.elian.taskproject.data.model.Task
import com.elian.taskproject.data.repository.TaskStaticRepository

class TaskListInteractor(private val listener: ITaskListContract.IOnInteractorListener) :
    ITaskListContract.IInteractor,
    ITaskListContract.IOnRepositoryCallback
{
    //region ITaskListContract.IInteractor

    override fun getList()
    {
        TaskStaticRepository.getList(this)
    }

    override fun remove(task: Task)
    {
        TaskStaticRepository.remove(this, task)
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

    override fun onRemoveSuccess(removedTask: Task)
    {
        listener.onRemoveSuccess(removedTask)
    }

    //endregion
}