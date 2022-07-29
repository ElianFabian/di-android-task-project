package com.elian.taskproject.data.database.dao

import androidx.room.*
import com.elian.taskproject.data.model.Task

@Dao
interface TaskDAO
{
    @Query("SELECT * FROM task_table WHERE id = :id")
    suspend fun select(id: Long): Task

    @Query("SELECT * FROM task_table")
    suspend fun selectAll(): List<Task>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(task: Task)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAll(tasks: List<Task>)

    @Delete
    suspend fun delete(task: Task)

    @Query("DELETE FROM task_table")
    suspend fun deleteAll()
}