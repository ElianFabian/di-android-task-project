package com.elian.taskproject.data.database.dao

import androidx.room.*
import com.elian.taskproject.data.model.Task

@Dao
interface TaskDAO
{
    @Query("SELECT * FROM task_table WHERE id = :id")
    fun select(id: Long): Task

    @Query("SELECT * FROM task_table")
    fun selectAll(): List<Task>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(task: Task): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(task: Task)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateAll(tasks: List<Task>)

    @Delete
    fun delete(task: Task)

    @Query("DELETE FROM task_table")
    fun deleteAll()
}