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
        }

        onTaskListStateChanged.invoke(taskList.isEmpty())
    }

    fun delete(taskToDelete: Task, position: Int)
    {
        repository.delete(taskToDelete, position)

        onDeleteTask.invoke(taskToDelete, position)

        onTaskListStateChanged.invoke(taskList.isEmpty())
    }

    fun undo(taskToRetrieve: Task, position: Int)
    {
        repository.undo(taskToRetrieve, position)

        onUndoDeleteTask.invoke(taskToRetrieve, position)

        onTaskListStateChanged.invoke(taskList.isEmpty())
    }

    fun checkTask(taskToCheck: Task, position: Int)
    {
        repository.checkTask(taskToCheck, position)

        onCheckTask.invoke(taskToCheck, position)

        onTaskListStateChanged.invoke(taskList.isEmpty())
    }

    fun uncheckTaskList(listOfCompletedTasks: List<Task>)
    {
        repository.uncheckTaskList(listOfCompletedTasks)

        onUncheckTaskList.invoke(listOfCompletedTasks)

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
