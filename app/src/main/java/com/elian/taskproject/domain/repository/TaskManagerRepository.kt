package com.elian.taskproject.domain.repository

import com.elian.taskproject.data.model.Task

interface TaskManagerRepository
{
    suspend fun add(taskToAdd: Task)
    suspend fun update(editedTask: Task, position: Int)
}
