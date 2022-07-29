package com.elian.taskproject.data.repository

import com.elian.taskproject.data.database.AppDatabase
import com.elian.taskproject.data.model.Task
import com.elian.taskproject.domain.repository.TaskRepository
import java.util.concurrent.Callable    
import java.util.concurrent.Future

class TaskRoomRepository : TaskRepository
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
        taskToCheck.check()

        execute { taskDAO.update(taskToCheck) }
    }

    override fun uncheckTaskList(checkedTaskList: List<Task>): List<Task>
    {
        val uncheckedTaskList = checkedTaskList.toList().onEach { it.uncheck() }

        execute { taskDAO.updateAll(uncheckedTaskList) }

        return uncheckedTaskList
    }

    override fun add(taskToAdd: Task)
    {
        execute { taskDAO.insert(taskToAdd) }
    }

    override fun update(editedTask: Task, position: Int)
    {
        execute { taskDAO.update(editedTask) }
    }
}
