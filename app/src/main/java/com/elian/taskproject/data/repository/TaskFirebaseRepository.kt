package com.elian.taskproject.data.repository

import com.elian.taskproject.data.AppInformation
import com.elian.taskproject.data.model.Task
import com.elian.taskproject.domain.repository.TaskListRepository
import com.elian.taskproject.domain.repository.TaskManagerRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object TaskFirebaseRepository :
    TaskListRepository,
    TaskManagerRepository
{
    private val firestore get() = Firebase.firestore

    private val userId get() = AppInformation.currentUser.email
    private val taskCollectionPath = "users/$userId/tasks"

    //region TaskListRepository

    override fun getTaskList(): List<Task>
    {
        var taskList = emptyList<Task>()

        firestore.collection(taskCollectionPath).get().addOnCompleteListener()
        {
            if (it.isSuccessful)
            {
                taskList = it.result.toObjects(Task::class.java)
            }
        }

        return taskList
    }

    override fun delete(taskToDelete: Task, position: Int)
    {
        val documentPath = "$taskCollectionPath/${taskToDelete.firebaseId}"

        firestore.document(documentPath).delete()
    }

    override fun undo(taskToRetrieve: Task, position: Int)
    {
        val documentPath = "$taskCollectionPath/${taskToRetrieve.firebaseId}"

        firestore.document(documentPath).set(taskToRetrieve)
    }

    override fun checkTask(taskToCheck: Task, position: Int,
    )
    {
        val documentPath = "$taskCollectionPath/${taskToCheck.firebaseId}"

        firestore.document(documentPath).update("completed", true)
    }

    override fun uncheckTaskList(completedTasks: List<Task>)
    {
        completedTasks.forEach()
        {
            val documentPath = "$taskCollectionPath/${it.firebaseId}"

            firestore.document(documentPath).set(it)
        }
    }

    override fun sortByNameAscending(): List<Task>
    {
        var sortedList = emptyList<Task>()

        firestore.collection(taskCollectionPath).get().addOnCompleteListener()
        {
            if (it.isSuccessful)
            {
                val list = it.result.toObjects(Task::class.java)

                sortedList = list.filter { task -> !task.isCompleted }.sortedBy { task -> task.name }
            }
        }

        return sortedList
    }

    override fun sortByNameDescending(): List<Task>
    {
        var sortedList = emptyList<Task>()

        firestore.collection(taskCollectionPath).get().addOnCompleteListener()
        {
            if (it.isSuccessful)
            {
                val list = it.result.toObjects(Task::class.java)

                sortedList = list.filter { task -> !task.isCompleted }.sortedByDescending { task -> task.name }
            }
        }

        return sortedList
    }

    //endregion

    //region TaskManagerRepository

    override fun add(taskToAdd: Task)
    {
        val documentPath = "${taskCollectionPath}/${taskToAdd.firebaseId}"

        firestore.document(documentPath).set(taskToAdd)
    }

    override fun update(editedTask: Task, position: Int)
    {
        val documentPath = "${taskCollectionPath}/${editedTask.firebaseId}"

        firestore.document(documentPath).set(editedTask)
    }

    //endregion
}