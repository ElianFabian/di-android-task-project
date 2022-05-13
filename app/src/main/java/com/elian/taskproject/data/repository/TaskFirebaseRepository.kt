package com.elian.taskproject.data.repository

import com.elian.taskproject.data.database.AppDatabase
import com.elian.taskproject.data.model.Task
import com.elian.taskproject.ui.tasklist.ITaskListContract
import com.elian.taskproject.ui.taskmanager.ITaskManagerContract
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object TaskFirebaseRepository :
    ITaskListContract.IRepository,
    ITaskManagerContract.IRepository
{
    private val firestore get() = Firebase.firestore

    private val userId get() = AppDatabase.getDatabase().userDAO.getUser().firebaseId
    private val taskCollectionPath = "users/${userId}/tasks"

    //region ITaskListContract.IRepository

    override fun getList(callback: ITaskListContract.IOnRepositoryCallback)
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

    override fun delete(callback: ITaskListContract.IOnRepositoryCallback, task: Task, position: Int)
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

    //region ITaskAddContract.IRepository

    override fun add(callback: ITaskManagerContract.IOnRepositoryCallback, task: Task)
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

    override fun edit(callback: ITaskManagerContract.IOnRepositoryCallback, editedTask: Task, position: Int)
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