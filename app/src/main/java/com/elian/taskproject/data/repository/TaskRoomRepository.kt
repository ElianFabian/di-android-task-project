package com.elian.taskproject.data.repository

import com.elian.taskproject.data.database.AppDatabase
import com.elian.taskproject.data.model.Task
import com.elian.taskproject.ui.tasklist.TaskListContract
import com.elian.taskproject.ui.taskmanager.TaskManagerContract
import java.util.concurrent.Callable
import java.util.concurrent.Future

object TaskRoomRepository :
    TaskListContract.Repository,
    TaskManagerContract.Repository
{
    private val taskDAO get() = AppDatabase.instance.taskDAO

    private val execute = AppDatabase.executorService::execute

    private fun <T> submit(callable: Callable<T>): Future<T>
    {
        return AppDatabase.executorService.submit(callable)
    }

    //region TaskListContract.Repository

    override fun getList(callback: TaskListContract.OnRepositoryCallback)
    {
        val list = submit { taskDAO.selectAll() }.get()

        if (list.isEmpty())
        {
            callback.onNoData()
        }
        else callback.onListSuccess(list)
    }

    override fun delete(callback: TaskListContract.OnRepositoryCallback, task: Task, position: Int)
    {
        execute { taskDAO.delete(task) }

        callback.onDeleteSuccess(task, position)
    }

    //endregion

    //region ITaskAddContract.Repository

    override fun add(callback: TaskManagerContract.OnRepositoryCallback, task: Task)
    {
        execute { taskDAO.insert(task) }

        callback.onAddSuccess()
    }

    override fun edit(callback: TaskManagerContract.OnRepositoryCallback, editedTask: Task, position: Int)
    {
        execute { taskDAO.update(editedTask) }

        callback.onEditSuccess()
    }

    //endregion
}
