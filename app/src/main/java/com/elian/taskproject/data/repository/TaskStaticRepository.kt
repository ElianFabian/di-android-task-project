package com.elian.taskproject.data.repository

import com.elian.taskproject.data.model.Task
import com.elian.taskproject.domain.repository.TaskRepository

class TaskStaticRepository : TaskRepository
{
    private val taskList = arrayListOf<Task>()


    override suspend fun getTaskList(): List<Task> = taskList.toList()

    override suspend fun delete(taskToDelete: Task, position: Int)
    {
        taskList.remove(taskToDelete)
    }

    override suspend fun undo(taskToRetrieve: Task, position: Int)
    {
        taskList.add(position, taskToRetrieve)
    }

    override suspend fun checkTask(taskToCheck: Task, position: Int)
    {
        taskToCheck.check()

        taskList[position] = taskToCheck
    }

    override suspend fun uncheckTaskList(checkedTaskList: List<Task>): List<Task>
    {
        taskList.replaceAll { it.apply { uncheck() } }

        return taskList.filter { it.isChecked }
    }

    override suspend fun add(taskToAdd: Task)
    {
        taskList.add(taskToAdd)
    }

    override suspend fun update(editedTask: Task, position: Int)
    {
        taskList[position] = editedTask
    }
}
