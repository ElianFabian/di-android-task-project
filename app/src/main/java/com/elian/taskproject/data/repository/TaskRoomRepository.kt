package com.elian.taskproject.data.repository

import com.elian.taskproject.data.database.AppDatabase
import com.elian.taskproject.data.database.dao.TaskDAO
import com.elian.taskproject.data.model.Task
import com.elian.taskproject.domain.repository.TaskRepository
import java.util.concurrent.Callable
import java.util.concurrent.Future
import javax.inject.Inject

class TaskRoomRepository @Inject constructor(private val taskDAO: TaskDAO) : TaskRepository
{
    override suspend fun getTaskList(): List<Task>
    {
        return taskDAO.selectAll()
    }

    override suspend fun delete(taskToDelete: Task, position: Int)
    {
        taskDAO.delete(taskToDelete)
    }

    override suspend fun undo(taskToRetrieve: Task, position: Int)
    {
        taskDAO.insert(taskToRetrieve)
    }

    override suspend fun checkTask(taskToCheck: Task, position: Int)
    {
        taskToCheck.check()

        taskDAO.update(taskToCheck)
    }

    override suspend fun uncheckTaskList(checkedTaskList: List<Task>): List<Task>
    {
        val uncheckedTaskList = checkedTaskList.toList().onEach { it.uncheck() }

        taskDAO.updateAll(uncheckedTaskList)

        return uncheckedTaskList
    }

    override suspend fun add(taskToAdd: Task)
    {
        taskDAO.insert(taskToAdd)
    }

    override suspend fun update(editedTask: Task, position: Int)
    {
        taskDAO.update(editedTask)
    }
}
