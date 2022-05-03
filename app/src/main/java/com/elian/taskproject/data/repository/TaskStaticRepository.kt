package com.elian.taskproject.data.repository

import com.elian.taskproject.data.model.Task
import com.elian.taskproject.ui.taskmanager.ITaskManagerContract
import com.elian.taskproject.ui.tasklist.ITaskListContract

object TaskStaticRepository :
    ITaskListContract.IRepository,
    ITaskManagerContract.IRepository
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

    override fun getList(callback: ITaskListContract.IOnRepositoryCallback)
    {
        if (taskList.size == 0)
        {
            callback.onNoData()
        }
        else callback.onListSuccess(taskList)
    }

    override fun delete(callback: ITaskListContract.IOnRepositoryCallback, task: Task)
    {
        val position = taskList.indexOf(task)

        taskList.remove(task)
        callback.onDeleteSuccess(task, position)
    }

    //endregion

    //region ITaskAddContract.IRepository

    override fun add(callback: ITaskManagerContract.IOnRepositoryCallback, task: Task)
    {
        taskList.add(task)
        callback.onAddSuccess()
    }

    override fun edit(callback: ITaskManagerContract.IOnRepositoryCallback, editedTask: Task, position: Int)
    {
        taskList[position] = editedTask
        callback.onEditSuccess()
    }

    //endregion
}
