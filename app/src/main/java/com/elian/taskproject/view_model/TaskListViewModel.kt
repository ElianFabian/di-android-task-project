package com.elian.taskproject.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elian.taskproject.data.model.Task
import com.elian.taskproject.data.repository.TaskRoomRepository
import com.elian.taskproject.domain.repository.TaskListRepository

class TaskListViewModel : ViewModel()
{
    private val repository: TaskListRepository = TaskRoomRepository

    private val uncompletedTaskList = mutableListOf<Task>()
    private val completedTaskList = mutableListOf<Task>()
    private val deletedTasksByPosition = linkedMapOf<Task, Int>()

    var isLoading = MutableLiveData<Boolean>()

    var onGetTaskList: (List<Task>) -> Unit = { }
    var onDeleteTask: (Task, Int) -> Unit = { _, _ -> }
    var onUndoDeleteTask: (Task, Int) -> Unit = { _, _ -> }
    var onCheckTask: (Task, Int) -> Unit = { _, _ -> }
    var onUncheckTaskList: (List<Task>) -> Unit = { }
    var onTaskListStateChanged: (Boolean) -> Unit = { }
    var onSortTaskListByName: (List<Task>) -> Unit = { }

    fun getTaskList()
    {
        isLoading.postValue(true)
        val list = repository.getTaskList()
        isLoading.postValue(false)

        if (list.isNotEmpty())
        {
            onGetTaskList.invoke(list)

            val completedList = list.filter { it.isCompleted }
            val uncompletedList = list.filter { !it.isCompleted }

            completedTaskList.clear()
            uncompletedTaskList.clear()
            completedTaskList.addAll(completedList)
            uncompletedTaskList.addAll(uncompletedList)
        }

        onTaskListStateChanged.invoke(uncompletedTaskList.isEmpty())
    }

    fun delete(taskToDelete: Task, position: Int)
    {
        repository.delete(taskToDelete, position)

        onDeleteTask.invoke(taskToDelete, position)

        uncompletedTaskList.remove(taskToDelete)
        deletedTasksByPosition[taskToDelete] = position

        onTaskListStateChanged.invoke(uncompletedTaskList.isEmpty())
    }

    fun undo()
    {
        if (deletedTasksByPosition.isEmpty()) return

        val lastDeletedTask = deletedTasksByPosition.keys.last()
        val position = deletedTasksByPosition.values.last()

        repository.undo(lastDeletedTask, position)

        onUndoDeleteTask.invoke(lastDeletedTask, position)

        uncompletedTaskList.add(lastDeletedTask)
        deletedTasksByPosition.remove(lastDeletedTask)

        onTaskListStateChanged.invoke(uncompletedTaskList.isEmpty())
    }

    fun checkTask(taskToCheck: Task, position: Int)
    {
        repository.checkTask(taskToCheck, position)

        onCheckTask.invoke(taskToCheck, position)

        uncompletedTaskList.remove(taskToCheck)
        completedTaskList.add(taskToCheck)

        onTaskListStateChanged.invoke(uncompletedTaskList.isEmpty())
    }

    fun uncheckTaskList()
    {
        if (completedTaskList.isEmpty()) return

        repository.uncheckTaskList(completedTaskList)

        val uncompletedList = completedTaskList.toList().onEach { it.markAsUncompleted() }
        uncompletedTaskList.addAll(uncompletedList)
        completedTaskList.clear()

        onUncheckTaskList.invoke(uncompletedList)

        onTaskListStateChanged.invoke(uncompletedTaskList.isEmpty())
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
