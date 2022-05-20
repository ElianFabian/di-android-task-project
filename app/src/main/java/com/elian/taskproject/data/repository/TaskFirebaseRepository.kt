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

    //endregion

    //region ITaskAddContract.Repository

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