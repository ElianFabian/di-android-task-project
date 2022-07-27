package com.elian.taskproject.domain.repository

import com.elian.taskproject.data.model.Task

interface TaskListRepository
{
    fun getTaskList(): List<Task>
    fun delete(taskToDelete: Task, position: Int)
    fun undo(taskToRetrieve: Task, position: Int)
    fun checkTask(taskToCheck: Task, position: Int)
    fun uncheckTaskList(completedTaskList: List<Task>)
    fun sortByNameAscending(): List<Task>
    fun sortByNameDescending(): List<Task>
}
