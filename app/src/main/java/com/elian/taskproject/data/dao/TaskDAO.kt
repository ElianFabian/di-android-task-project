package com.elian.taskproject.data.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.elian.taskproject.data.model.Task

interface TaskDAO
{
    @Query("SELECT * FROM task_table WHERE id = :id")
    fun select(id: Long): Task

    @Query("SELECT * FROM task_table")
    fun selectAll() : List<Task>

    @Insert
    fun insert(task: Task): Long

    @Update
    fun update(task: Task)

    @Delete
    fun delete(task: Task)

    @Query("DELETE FROM task_table")
    fun deleteAll()
}