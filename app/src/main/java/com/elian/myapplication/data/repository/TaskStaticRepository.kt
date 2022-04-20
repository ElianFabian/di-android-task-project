package com.elian.myapplication.data.repository

import com.elian.myapplication.data.model.Task
import com.elian.myapplication.ui.taskadd.ITaskAddContract
import com.elian.myapplication.ui.tasklist.ITaskListContract
import java.util.*

class TaskStaticRepository :
    ITaskListContract.IRepository,
    ITaskAddContract.IRepository
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
            endDateEstimated = getRandomDateAroundCurrentDate()
        ),
        Task(
            name = "task2",
            description = "It's a task",
            importance = Task.Importance.values().random(),
            endDateEstimated = getRandomDateAroundCurrentDate()
        ),
        Task(
            name = "task3",
            description = "It's a task",
            importance = Task.Importance.values().random(),
            endDateEstimated = getRandomDateAroundCurrentDate()
        ),
        Task(
            name = "task4",
            description = "It's a task",
            importance = Task.Importance.values().random(),
            endDateEstimated = getRandomDateAroundCurrentDate()
        )
    )

    //region Methods

    fun add(task: Task)
    {
        taskList.add(task)
    }

    /**
     * Returns a random date from the current date before or after the indicated amount of years.
     */
    private fun getRandomDateAroundCurrentDate(years: Long = 10): Long
    {
        val oneYearInMillis = 31_556_900_000

        val yearsInMillis = years * oneYearInMillis
        val range = -yearsInMillis..yearsInMillis

        return calendar.timeInMillis + range.random()
    }

    //endregion

    //region ITaskListContract.IRepository

    override fun getTaskList(callback: ITaskListContract.IOnRepositoryCallback)
    {
        callback.onSuccess(taskList)
    }

    //endregion

    //region ITaskAddContract.IRepository

    override fun add(callback: ITaskAddContract.IOnRepositoryCallback, task: Task)
    {
        TODO("Not yet implemented")
    }

    //endregion
}
