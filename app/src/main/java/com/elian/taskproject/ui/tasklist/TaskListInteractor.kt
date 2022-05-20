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

    override fun undo(taskToRetrieve: Task, position: Int)
    {
        repository.undo(this, taskToRetrieve, position)
    }

    override fun changeCompletedState(taskToChangeCompletedState: Task, position: Int, newState: Boolean)
    {
        repository.changeCompletedState(this, taskToChangeCompletedState, position, newState)
    }

    override fun onGetListSuccess(listFromRepository: List<Task>)
    {
        listener.onGetListSuccess(listFromRepository)
    }

    override fun onGetListFailure()
    {
        listener.onGetListFailure()
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

    override fun onUndoSuccess(retrievedTask: Task, position: Int)
    {
        listener.onUndoSuccess(retrievedTask, position)
    }

    override fun onUndoFailure()
    {
        listener.onUndoFailure()
    }

    override fun onCompletedStateChangedSuccess(completedStateChangedTask: Task, position: Int)
    {
        listener.onCompletedStateChangedSuccess(completedStateChangedTask, position)
    }

    override fun onCompletedStateChangedFailure()
    {
        listener.onCompletedStateChangedFailure()
    }

    //endregion
}