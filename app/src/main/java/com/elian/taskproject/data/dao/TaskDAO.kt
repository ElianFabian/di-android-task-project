package com.elian.taskproject.data.dao

import androidx.room.Insert
import androidx.room.Update
import com.elian.taskproject.data.model.Task

interface TaskDAO
{
    @Insert
    fun insert(task: Task)
    
    @Update
    fun update(task: Task)
}