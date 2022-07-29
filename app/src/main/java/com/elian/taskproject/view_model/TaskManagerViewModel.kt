package com.elian.taskproject.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elian.taskproject.data.model.Task
import com.elian.taskproject.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskManagerViewModel @Inject constructor(private val repository: TaskRepository) : ViewModel()
{
    var onTaskAdded: (Task) -> Unit = {}
    var onTaskUpdated: (Task) -> Unit = {}

    var onValidateTask: () -> Unit = {}
    var onNameEmptyError: () -> Unit = {}
    var onDateEmptyError: () -> Unit = {}

    fun add(taskToAdd: Task)
    {
        if (validateTask(taskToAdd)) return

        viewModelScope.launch { repository.add(taskToAdd) }

        onTaskAdded(taskToAdd)
    }

    fun update(updatedTask: Task, position: Int)
    {
        if (validateTask(updatedTask)) return

        viewModelScope.launch { repository.update(updatedTask, position) }

        onTaskUpdated(updatedTask)
    }

    /**
     * Returns true if the task is not valid, false otherwise.
     */
    private fun validateTask(task: Task): Boolean
    {
        onValidateTask()

        return when
        {
            task.name.isEmpty()  -> onNameEmptyError().run { true }
            task.endDate == null -> onDateEmptyError().run { true }

            else                 -> false
        }
    }
}
