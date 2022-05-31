package com.elian.taskproject.data.repository

import com.elian.taskproject.data.AppInformation
import com.elian.taskproject.data.model.Task
import com.elian.taskproject.ui.tasklist.TaskListContract
import com.elian.taskproject.ui.taskmanager.TaskManagerContract
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object TaskFirebaseRepository :
    TaskListContract.Repository,
    TaskManagerContract.Repository
{
    private val firestore get() = Firebase.firestore

    private val userId get() = AppInformation.currentUser.email
    private val taskCollectionPath = "users/$userId/tasks"

    //region TaskListContract.Repository

    override fun getList(callback: TaskListContract.OnGetListCallback)
    {
        firestore.collection(taskCollectionPath).get().addOnCompleteListener()
        {
            if (it.isSuccessful)
            {
                val tasks = it.result.toObjects(Task::class.java)

                if (tasks.isNotEmpty()) callback.onGetListSuccess(tasks)
                else callback.onNoData()
            }
            else callback.onGetListFailure()
        }
    }

    override fun delete(callback: TaskListContract.OnDeleteCallback, taskToDelete: Task, position: Int)
    {
        val documentPath = "$taskCollectionPath/${taskToDelete.firebaseId}"

        firestore.document(documentPath).delete()
            .addOnSuccessListener { callback.onDeleteSuccess(taskToDelete, position) }
            .addOnFailureListener { callback.onDeleteFailure() }
    }

    override fun undo(callback: TaskListContract.OnUndoCallback, taskToRetrieve: Task, position: Int)
    {
        val documentPath = "$taskCollectionPath/${taskToRetrieve.firebaseId}"

        firestore.document(documentPath).set(taskToRetrieve)
            .addOnSuccessListener { callback.onUndoSuccess(taskToRetrieve, position) }
            .addOnFailureListener { callback.onUndoFailure() }
    }

    override fun changeCompletedState(
        callback: TaskListContract.OnCompletedStateChangedCallback,
        taskToChangeCompletedState: Task,
        position: Int,
        newState: Boolean,
    )
    {
        val documentPath = "$taskCollectionPath/${taskToChangeCompletedState.firebaseId}"

        firestore.document(documentPath).update("completed", true)
            .addOnSuccessListener()
            {
                taskToChangeCompletedState.isCompleted = newState
                callback.onCompletedStateChangedSuccess(taskToChangeCompletedState, position)
            }
            .addOnFailureListener()
            {
                callback.onCompletedStateChangedFailure()
            }
    }

    override fun markAsUncompleted(callback: TaskListContract.OnMarkAsUncompletedCallback, completedTasks: List<Task>)
    {
        completedTasks.forEach()
        {
            val documentPath = "$taskCollectionPath/${it.firebaseId}"

            val uncompletedTasks = completedTasks.toList().onEach { task -> task.markAsUncompleted() }

            firestore.document(documentPath).set(it)
                .addOnSuccessListener()
                {
                    callback.onMarkAsUncompletedSuccess(uncompletedTasks)
                }
                .addOnFailureListener()
                {
                    callback.onMarkAsUncompletedFailure()
                }
        }
    }

    override fun sortByNameAscending(callback: TaskListContract.OnSortByNameAscendingCallback)
    {
        firestore.collection(taskCollectionPath).get().addOnCompleteListener()
        {
            if (it.isSuccessful)
            {
                val list = it.result.toObjects(Task::class.java)
                val sortedList = list.filter { task -> !task.isCompleted }.sortedBy { task -> task.name }

                callback.onSortByNameAscendingSuccess(sortedList)
            }
            else callback.onSortByNameAscendingFailure()
        }
    }

    override fun sortByNameDescending(callback: TaskListContract.OnSortByNameDescendingCallback)
    {
        firestore.collection(taskCollectionPath).get().addOnCompleteListener()
        {
            if (it.isSuccessful)
            {
                val list = it.result.toObjects(Task::class.java)
                val sortedList = list.filter { task -> !task.isCompleted }.sortedByDescending { task -> task.name }

                callback.onSortByNameDescendingSuccess(sortedList)
            }
            else callback.onSortByNameDescendingFailure()
        }
    }

    //endregion

    //region TaskManagerContract.Repository

    override fun add(callback: TaskManagerContract.OnAddCallback, task: Task)
    {
        val documentPath = "${taskCollectionPath}/${task.firebaseId}"

        firestore.document(documentPath).set(task)
            .addOnSuccessListener { callback.onAddSuccess() }
            .addOnFailureListener { callback.onAddFailure() }
    }

    override fun edit(callback: TaskManagerContract.OnEditCallback, editedTask: Task, position: Int)
    {
        val documentPath = "${taskCollectionPath}/${editedTask.firebaseId}"

        firestore.document(documentPath).set(editedTask)
            .addOnSuccessListener { callback.onEditSuccess() }
            .addOnFailureListener { callback.onEditFailure() }
    }

    //endregion
}