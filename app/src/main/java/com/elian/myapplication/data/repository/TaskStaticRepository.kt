package com.elian.myapplication.data.repository

import com.elian.myapplication.data.model.Task
import com.elian.myapplication.ui.tasklist.ITaskListContract
import java.util.*

class TaskStaticRepository : ITaskListContract.IRepository
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
            description = "It's a task",
            importance = Task.Importance.values().random(),
            endDateEstimated = getRandomDate()
        ),
        Task(
            name = "task2",
            description = "It's a task",
            importance = Task.Importance.values().random(),
            endDateEstimated = getRandomDate()
        ),
        Task(
            name = "task3",
            description = "It's a task",
            importance = Task.Importance.values().random(),
            endDateEstimated = getRandomDate()
        ),
        Task(
            name = "task4",
            description = "It's a task",
            importance = Task.Importance.values().random(),
            endDateEstimated = getRandomDate()
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
    private fun getRandomDate(years: Long = 10): Long =
        calendar.timeInMillis - (years * -31_556_900_000..31_556_900_000).random()

    //endregion

    //region ITaskListContract.IRepository

    override fun load()
    {
        TODO("Not yet implemented")
    }

    //endregion
}