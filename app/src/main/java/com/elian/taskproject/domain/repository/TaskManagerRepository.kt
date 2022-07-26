package com.elian.taskproject.domain.repository

import com.elian.taskproject.data.model.Task

interface TaskManagerRepository
{
    fun add(taskToAdd: Task)
    fun update(editedTask: Task, position: Int)
}
