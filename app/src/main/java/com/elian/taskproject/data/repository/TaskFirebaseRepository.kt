package com.elian.taskproject.data.repository

import com.elian.taskproject.data.AppInformation
import com.elian.taskproject.data.model.Task
import com.elian.taskproject.domain.repository.TaskRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TaskFirebaseRepository : TaskRepository
{
    private val firestore get() = Firebase.firestore

    private val userId get() = AppInformation.currentUser.email
    private val taskCollectionPath = "users/$userId/tasks"


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

    override fun checkTask(
        taskToCheck: Task, position: Int,
    )
    {
        val documentPath = "$taskCollectionPath/${taskToCheck.firebaseId}"

        firestore.document(documentPath).update("isChecked", true)
    }

    override fun uncheckTaskList(checkedTaskList: List<Task>): List<Task>
    {
        val uncheckedTaskList = mutableListOf<Task>()

        checkedTaskList.forEach()
        {
            val documentPath = "$taskCollectionPath/${it.firebaseId}"

            it.uncheck()

            firestore.document(documentPath).set(it)

            uncheckedTaskList.add(it)
        }

        return uncheckedTaskList
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