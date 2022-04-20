package com.elian.taskproject.data.repository

import com.elian.taskproject.data.model.Task
import com.elian.taskproject.ui.taskadd.ITaskAddContract
import com.elian.taskproject.ui.tasklist.ITaskListContract

object TaskStaticRepository :
    ITaskListContract.IRepository,
    ITaskAddContract.IRepository
{
    private val taskList = arrayListOf<Task>()

//    private var calendar: Calendar = Calendar.getInstance()
    
    //region Methods

    /**
     * Returns a random date from the current date before or after the indicated amount of years.
     */
//    private fun getRandomDateAroundCurrentDate(years: Long = 10): Long
//    {
//        val oneYearInMillis = 31_556_900_000
//
//        val yearsInMillis = years * oneYearInMillis
//        val range = -yearsInMillis..yearsInMillis
//
//        return calendar.timeInMillis + range.random()
//    }

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
        taskList.add(task)
        callback.onSuccess()
    }

    //endregion
}
