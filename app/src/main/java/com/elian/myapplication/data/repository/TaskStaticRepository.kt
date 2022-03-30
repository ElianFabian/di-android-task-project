package com.elian.myapplication.data.repository

import com.elian.myapplication.data.model.Task

class TaskStaticRepository
{
    companion object
    {
        public val instance = TaskStaticRepository()
    }

    private val taskList = arrayListOf(
        Task(
            name = "tarea1",
            description = "es una tarea",
            importance = Task.Importance.HIGH,
            endDate = 12345678
        )
    )

    //region Methods

    fun add(task: Task)
    {
        taskList.add(task)
    }

    fun getList() = taskList

    //endregion
}