package com.elian.taskproject.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elian.taskproject.data.model.Task
import com.elian.taskproject.data.repository.TaskRoomRepository
import com.elian.taskproject.domain.repository.TaskListRepository

class TaskListViewModel : ViewModel()
{
    private val repository: TaskListRepository = TaskRoomRepository

    private val uncheckedTaskList = mutableListOf<Task>()
    private val checkedTaskList = mutableListOf<Task>()
    private val deletedTasksByPosition = linkedMapOf<Task, Int>()

    var isLoading = MutableLiveData<Boolean>()

    var onGetTaskList: (List<Task>) -> Unit = { }
    var onDeleteTask: (Task, Int) -> Unit = { _, _ -> }
    var onUndoDeleteTask: (Task, Int) -> Unit = { _, _ -> }
    var onCheckTask: (Task, Int) -> Unit = { _, _ -> }
    var onUncheckTaskList: (List<Task>) -> Unit = { }
    var onUncheckedTaskListStateChanged: (Boolean) -> Unit = { }
    var onSortTaskListByName: (List<Task>) -> Unit = { }

    fun getTaskList()
    {
        isLoading.postValue(true)
        val list = repository.getTaskList()
        isLoading.postValue(false)

        if (list.isNotEmpty())
        {
            onGetTaskList.invoke(list)

            val checkedList = list.filter { it.isChecked }
            val uncheckedList = list.filter { !it.isChecked }

            checkedTaskList.clear()
            uncheckedTaskList.clear()
            checkedTaskList.addAll(checkedList)
            uncheckedTaskList.addAll(uncheckedList)
        }

        onUncheckedTaskListStateChanged.invoke(uncheckedTaskList.isEmpty())
    }

    fun delete(taskToDelete: Task, position: Int)
    {
        repository.delete(taskToDelete, position)
        
        uncheckedTaskList.remove(taskToDelete)
        deletedTasksByPosition[taskToDelete] = position

        onDeleteTask.invoke(taskToDelete, position)
        onUncheckedTaskListStateChanged.invoke(uncheckedTaskList.isEmpty())
    }

    fun undo()
    {
        if (deletedTasksByPosition.isEmpty()) return

        val lastDeletedTask = deletedTasksByPosition.keys.last()
        val position = deletedTasksByPosition.values.last()

        repository.undo(lastDeletedTask, position)
        
        uncheckedTaskList.add(lastDeletedTask)
        deletedTasksByPosition.remove(lastDeletedTask)

        onUndoDeleteTask.invoke(lastDeletedTask, position)
        onUncheckedTaskListStateChanged.invoke(uncheckedTaskList.isEmpty())
    }

    fun checkTask(taskToCheck: Task, position: Int)
    {
        repository.checkTask(taskToCheck, position)
        
        uncheckedTaskList.remove(taskToCheck)
        checkedTaskList.add(taskToCheck)

        onCheckTask.invoke(taskToCheck, position)
        onUncheckedTaskListStateChanged.invoke(uncheckedTaskList.isEmpty())
    }

    fun uncheckList()
    {
        if (checkedTaskList.isEmpty()) return

        repository.uncheckTaskList(checkedTaskList)

        val uncheckedList = checkedTaskList.toList().onEach { it.uncheck() }
        uncheckedTaskList.addAll(uncheckedList)
        checkedTaskList.clear()

        onUncheckTaskList.invoke(uncheckedList)
        onUncheckedTaskListStateChanged.invoke(uncheckedTaskList.isEmpty())
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
