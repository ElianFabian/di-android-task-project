package com.elian.taskproject.domain.repository

import com.elian.taskproject.data.model.Task

interface TaskListRepository
{
    suspend fun getTaskList(): List<Task>
    suspend fun delete(taskToDelete: Task, position: Int)
    suspend fun undo(taskToRetrieve: Task, position: Int)
    suspend fun checkTask(taskToCheck: Task, position: Int)
    suspend fun uncheckTaskList(checkedTaskList: List<Task>): List<Task>
}
