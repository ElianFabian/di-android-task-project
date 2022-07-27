package com.elian.taskproject.data.repository

import com.elian.taskproject.data.model.Task
import com.elian.taskproject.domain.repository.TaskListRepository
import com.elian.taskproject.domain.repository.TaskManagerRepository

object TaskStaticRepository :
    TaskListRepository,
    TaskManagerRepository
{
    private val taskList = arrayListOf<Task>()

    //region TaskListContract.Repository

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
        taskToCheck.markAsCompleted()
        
        taskList[position] = taskToCheck
    }

    override fun uncheckTaskList(completedTaskList: List<Task>)
    {
        taskList.replaceAll { it.apply { markAsUncompleted() } }
    }

    override fun sortByNameAscending(): List<Task>
    {
        return taskList.filter { !it.isCompleted }.sortedBy { it.name }
    }

    override fun sortByNameDescending(): List<Task>
    {
        return taskList.filter { !it.isCompleted }.sortedByDescending { it.name }
    }

    //endregion

    //region TaskManagerContract.Repository

    override fun add(taskToAdd: Task)
    {
        taskList.add(taskToAdd)
    }

    override fun update(editedTask: Task, position: Int)
    {
        taskList[position] = editedTask
    }

    //endregion
}
