package com.elian.taskproject.view_model

import androidx.lifecycle.ViewModel
import com.elian.taskproject.data.model.Task
import com.elian.taskproject.data.repository.TaskRoomRepository
import com.elian.taskproject.domain.repository.TaskManagerRepository

class TaskManagerViewModel : ViewModel()
{
    private val repository: TaskManagerRepository = TaskRoomRepository

    var onTaskAdded: (Task) -> Unit = {}
    var onTaskUpdated: (Task) -> Unit = {}

    var onValidateTask: () -> Unit = {}
    var onNameEmptyError: () -> Unit = {}
    var onDateEmptyError: () -> Unit = {}

    fun add(taskToAdd: Task)
    {
        if (validateTask(taskToAdd)) return

        repository.add(taskToAdd)

        onTaskAdded.invoke(taskToAdd)
    }

    fun update(updatedTask: Task, position: Int)
    {
        if (validateTask(updatedTask)) return

        repository.update(updatedTask, position)

        onTaskUpdated.invoke(updatedTask)
    }

    /**
     * Returns true if the task is not valid, false otherwise.
     */
    private fun validateTask(task: Task): Boolean
    {
        onValidateTask.invoke()

        return when
        {
            task.name.isEmpty()  -> onNameEmptyError().run { true }
            task.endDate == null -> onDateEmptyError().run { true }

            else                 -> false
        }
    }
}
