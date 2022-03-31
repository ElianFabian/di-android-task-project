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
            name = "task1",
            description = "es una task",
            importance = Task.Importance.values().random(),
            endDate = getRandomDate()
        ),
        Task(
            name = "task2",
            description = "es una task",
            importance = Task.Importance.values().random(),
            endDate = getRandomDate()
        ),
        Task(
            name = "task3",
            description = "es una task",
            importance = Task.Importance.values().random(),
            endDate = getRandomDate()
        ),
        Task(
            name = "task4",
            description = "es una task",
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
     * Returns a random date from the current date before or after the indicated amount of years.
     */
    private fun getRandomDate(years: Long = 10): Long = calendar.timeInMillis - (years * - 31_556_900_000.. 31_556_900_000).random()

    //endregion
}