package com.elian.taskproject.data.repository

import com.elian.taskproject.data.model.Task
import com.elian.taskproject.domain.repository.TaskRepository

class TaskStaticRepository : TaskRepository
{
    private val taskList = arrayListOf<Task>()


    override fun getTaskList(): List<Task> = taskList.toList()

    override fun delete(taskToDelete: Task, position: Int)
    {
        taskList.remove(taskToDelete)
    }

    override fun undo(taskToRetrieve: Task, position: Int)
    {
        taskList.add(position, taskToRetrieve)
    }

    override fun checkTask(taskToCheck: Task, position: Int)
    {
        taskToCheck.check()

        taskList[position] = taskToCheck
    }

    override fun uncheckTaskList(checkedTaskList: List<Task>): List<Task>
    {
        taskList.replaceAll { it.apply { uncheck() } }

        return taskList.filter { it.isChecked }
    }

    override fun add(taskToAdd: Task)
    {
        taskList.add(taskToAdd)
    }

    override fun update(editedTask: Task, position: Int)
    {
        taskList[position] = editedTask
    }
}
