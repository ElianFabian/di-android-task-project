package com.elian.taskproject.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elian.taskproject.data.model.Task
import com.elian.taskproject.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(private val repository: TaskRepository) : ViewModel()
{
    private val uncheckedTaskList = mutableListOf<Task>()
    private val checkedTaskList = mutableListOf<Task>()
    private val deletedTasksByPosition = linkedMapOf<Task, Int>()

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    var onGetTaskList: (List<Task>) -> Unit = { }
    var onDeleteTask: (Task, Int) -> Unit = { _, _ -> }
    var onUndoDeleteTask: (Task, Int) -> Unit = { _, _ -> }
    var onCheckTask: (Task, Int) -> Unit = { _, _ -> }
    var onUncheckTaskList: (List<Task>) -> Unit = { }
    var onUncheckedTaskListStateChanged: (Boolean) -> Unit = { }
    var onSortTaskListByName: (List<Task>) -> Unit = { }


    fun getTaskList()
    {
        viewModelScope.launch()
        {
            _isLoading.postValue(true)
            val list = repository.getTaskList()
            _isLoading.postValue(false)

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
    }

    fun delete(taskToDelete: Task, position: Int)
    {
        viewModelScope.launch { repository.delete(taskToDelete, position) }

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

        viewModelScope.launch { repository.undo(lastDeletedTask, position) }

        uncheckedTaskList.add(lastDeletedTask)
        deletedTasksByPosition.remove(lastDeletedTask)

        onUndoDeleteTask.invoke(lastDeletedTask, position)
        onUncheckedTaskListStateChanged.invoke(uncheckedTaskList.isEmpty())
    }

    fun checkTask(taskToCheck: Task, position: Int)
    {
        viewModelScope.launch { repository.checkTask(taskToCheck, position) }

        uncheckedTaskList.remove(taskToCheck)
        checkedTaskList.add(taskToCheck)

        onCheckTask.invoke(taskToCheck, position)
        onUncheckedTaskListStateChanged.invoke(uncheckedTaskList.isEmpty())
    }

    fun uncheckList()
    {
        if (checkedTaskList.isEmpty()) return

        viewModelScope.launch()
        {
            val uncheckedList = repository.uncheckTaskList(checkedTaskList)

            uncheckedTaskList.addAll(uncheckedList)
            checkedTaskList.clear()

            onUncheckTaskList.invoke(uncheckedList)
            onUncheckedTaskListStateChanged.invoke(uncheckedTaskList.isEmpty())
        }
    }

    fun sortByNameAscending()
    {
        val sortedList = uncheckedTaskList.sortedBy { it.name }

        onSortTaskListByName.invoke(sortedList)
    }

    fun sortByNameDescending()
    {
        val sortedList = uncheckedTaskList.sortedByDescending { it.name }

        onSortTaskListByName.invoke(sortedList)
    }
}
