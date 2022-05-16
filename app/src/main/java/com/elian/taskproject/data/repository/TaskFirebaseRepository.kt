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

    override fun getList(callback: TaskListContract.OnRepositoryCallback)
    {
        firestore.collection(taskCollectionPath).get().addOnCompleteListener()
        {
            if (it.isSuccessful)
            {
                val tasks = it.result.toObjects(Task::class.java) as List<Task>

                if (tasks.isNotEmpty()) callback.onListSuccess(tasks)
                else callback.onNoData()
            }
            else callback.onListFailure()
        }
    }

    override fun delete(callback: TaskListContract.OnRepositoryCallback, task: Task, position: Int)
    {
        val documentPath = "$taskCollectionPath/${task.firebaseId}"

        firestore.document(documentPath).delete().addOnCompleteListener()
        {
            if (it.isSuccessful)
            {
                callback.onDeleteSuccess(task, position)
            }
            else callback.onDeleteFailure()
        }
    }

    //endregion

    //region ITaskAddContract.Repository

    override fun add(callback: TaskManagerContract.OnRepositoryCallback, task: Task)
    {
        val documentPath = "${taskCollectionPath}/${task.firebaseId}"

        firestore.document(documentPath).set(task).addOnCompleteListener()
        {
            if (it.isSuccessful)
            {
                callback.onAddSuccess()
            }
            else callback.onAddFailure()
        }
    }

    override fun edit(callback: TaskManagerContract.OnRepositoryCallback, editedTask: Task, position: Int)
    {
        val documentPath = "${taskCollectionPath}/${editedTask.firebaseId}"

        firestore.document(documentPath).set(editedTask).addOnCompleteListener()
        {
            if (it.isSuccessful)
            {
                callback.onEditSuccess()
            }
            else callback.onEditFailure()
        }
    }

    //endregion
}