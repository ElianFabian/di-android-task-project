package com.elian.taskproject.data.repository

import com.elian.taskproject.data.database.AppDatabase
import com.elian.taskproject.data.model.Task
import com.elian.taskproject.domain.repository.TaskListRepository
import com.elian.taskproject.domain.repository.TaskManagerRepository
import java.util.concurrent.Callable
import java.util.concurrent.Future

object TaskRoomRepository :
    TaskListRepository,
    TaskManagerRepository
{
    private val taskDAO get() = AppDatabase.instance.taskDAO

    private fun execute(runnable: Runnable)
    {
        AppDatabase.executorService.execute(runnable)
    }

    private fun <T> submit(callable: Callable<T>): Future<T>
    {
        return AppDatabase.executorService.submit(callable)
    }

    //region TaskListRepository

    override fun getTaskList(): List<Task>
    {
        return submit { taskDAO.selectAll() }.get()
    }

    override fun delete(taskToDelete: Task, position: Int)
    {
        execute { taskDAO.delete(taskToDelete) }
    }

    override fun undo(taskToRetrieve: Task, position: Int)
    {
        execute { taskDAO.insert(taskToRetrieve) }
    }

    override fun checkTask(taskToCheck: Task, position: Int)
    {
        taskToCheck.isCompleted = true

        execute { taskDAO.update(taskToCheck) }
    }

    override fun uncheckTaskList(completedTasks: List<Task>)
    {
        val uncompletedTasks = completedTasks.toList().onEach { task -> task.markAsUncompleted() }

        execute { taskDAO.updateAll(uncompletedTasks) }
    }

    override fun sortByNameAscending(): List<Task>
    {
        val list = submit { taskDAO.selectAll() }.get()

        return list.filter { !it.isCompleted }.sortedBy { it.name }
    }

    override fun sortByNameDescending(): List<Task>
    {
        val list = submit { taskDAO.selectAll() }.get()

        return list.filter { !it.isCompleted }.sortedByDescending { it.name }
    }

    //endregion

    //region TaskManagerRepository

    override fun add(taskToAdd: Task)
    {
        execute { taskDAO.insert(taskToAdd) }
    }

    override fun update(editedTask: Task, position: Int)
    {
        execute { taskDAO.update(editedTask) }
    }

    //endregion
}
