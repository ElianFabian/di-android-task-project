package com.elian.myapplication.data.repository

import com.elian.myapplication.data.model.Task
import java.util.*

class TaskStaticRepository
{
    companion object
    {
        val instance = TaskStaticRepository()
        
        private lateinit var calendar: Calendar
    }
    
    init
    {
         calendar = Calendar.getInstance()
    }

    private val taskList = arrayListOf(
        Task(
            name = "tarea1",
            description = "es una tarea",
            importance = Task.Importance.values().random(),
            endDate = getRandomDate()
        ),
        Task(
            name = "tarea2",
            description = "es una tarea",
            importance = Task.Importance.values().random(),
            endDate = getRandomDate()
        ),
        Task(
            name = "tarea3",
            description = "es una tarea",
            importance = Task.Importance.values().random(),
            endDate = getRandomDate()
        ),
        Task(
            name = "tarea4",
            description = "es una tarea",
            importance = Task.Importance.values().random(),
            endDate = getRandomDate()
        )
    )

    //region Methods

    fun add(task: Task)
    {
        taskList.add(task)
    }

    fun getList() = taskList

    /**
     * Returns a date from now until the indicated years back.
     */
    private fun getRandomDate(years: Long = 10): Long = calendar.timeInMillis - (years * 0.. 31_556_900_000).random()

    //endregion
}