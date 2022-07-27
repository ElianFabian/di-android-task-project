package com.elian.taskproject.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elian.taskproject.data.model.Task
import com.elian.taskproject.data.repository.TaskRoomRepository
import com.elian.taskproject.domain.repository.TaskListRepository

class TaskListViewModel : ViewModel()
{
    private val repository: TaskListRepository = TaskRoomRepository

    private lateinit var taskList: List<Task>

    private val deletedTasksByPosition = linkedMapOf<Task, Int>()
    private val completedTaskSet = mutableSetOf<Task>()

    var isLoading = MutableLiveData<Boolean>()

    var onGetTaskList: (List<Task>) -> Unit = { }
    var onDeleteTask: (Task, Int) -> Unit = { _, _ -> }
    var onUndoDeleteTask: (Task, Int) -> Unit = { _, _ -> }
    var onCheckTask: (Task, Int) -> Unit = { _, _ -> }
    var onUncheckTaskList: (List<Task>) -> Unit = { }
    var onTaskListStateChanged: (Boolean) -> Unit = { }
    var onSortTaskListByName: (List<Task>) -> Unit = { }


    fun setUncompletedTaskList(taskList: List<Task>)
    {
        this.taskList = taskList
    }


    fun getTaskList()
    {
        isLoading.postValue(true)
        val list = repository.getTaskList()
        isLoading.postValue(false)

        if (list.isNotEmpty())
        {
            onGetTaskList.invoke(list)

            val completedTaskList = list.filter { it.isCompleted }

            completedTaskList.forEach { completedTaskSet.add(it) }
        }

        onTaskListStateChanged.invoke(taskList.isEmpty())
    }

    fun delete(taskToDelete: Task, position: Int)
    {
        repository.delete(taskToDelete, position)

        onDeleteTask.invoke(taskToDelete, position)

        deletedTasksByPosition[taskToDelete] = position

        onTaskListStateChanged.invoke(taskList.isEmpty())
    }

    fun undo()
    {
        if (deletedTasksByPosition.isEmpty()) return

        val lastDeletedTask = deletedTasksByPosition.keys.last()
        val position = deletedTasksByPosition.values.last()

        repository.undo(lastDeletedTask, position)

        onUndoDeleteTask.invoke(lastDeletedTask, position)

        deletedTasksByPosition.remove(lastDeletedTask)

        onTaskListStateChanged.invoke(taskList.isEmpty())
    }

    fun checkTask(taskToCheck: Task, position: Int)
    {
        repository.checkTask(taskToCheck, position)

        onCheckTask.invoke(taskToCheck, position)

        completedTaskSet.add(taskToCheck)

        onTaskListStateChanged.invoke(taskList.isEmpty())
    }

    fun uncheckTaskList()
    {
        if (completedTaskSet.isEmpty()) return

        repository.uncheckTaskList(completedTaskSet.toList())

        onUncheckTaskList.invoke(completedTaskSet.toList())

        completedTaskSet.clear()

        onTaskListStateChanged.invoke(taskList.isEmpty())
    }

    fun sortByNameAscending()
    {
        val sortedList = repository.sortByNameAscending()

        onSortTaskListByName.invoke(sortedList)
    }

    fun sortByNameDescending()
    {
        val sortedList = repository.sortByNameDescending()

        onSortTaskListByName.invoke(sortedList)
    }
}
